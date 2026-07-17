package com.namiq.msbook.service;

import com.namiq.msbook.config.RabbitMQConfig;
import com.namiq.msbook.dao.entity.Book;
import com.namiq.msbook.dao.entity.Borrow;
import com.namiq.msbook.dao.repository.BookRepository;
import com.namiq.msbook.dao.repository.BorrowRepository;
import com.namiq.msbook.dto.request.BorrowRequest;
import com.namiq.msbook.dto.response.BorrowResponse;
import com.namiq.msbook.dto.response.UserResponse;
import com.namiq.msbook.enums.EventType;
import com.namiq.msbook.enums.Role;
import com.namiq.msbook.enums.Status;
import com.namiq.msbook.event.LibraryEvent;
import com.namiq.msbook.exception.*;
import com.namiq.msbook.mapper.BorrowMapper;
import com.namiq.msbook.publisher.LibraryEventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserClient userClient;
    private final BorrowMapper borrowMapper;
    private final LibraryEventPublisher publisher;

    private void checkAdmin(String token) {
        UserResponse user = userClient.getCUrrentUser(token);

        if (user.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedOperationException("Only admins can perform this operation.");
        }
    }

    @Transactional

    public BorrowResponse borrowBook(String token, BorrowRequest borrowRequest) {


        Book book = bookRepository.findById(borrowRequest.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("No available copies for this book");
        }
        UserResponse user = userClient.getCUrrentUser(token);
        boolean exists = borrowRepository.existsByUserIdAndBookIdAndStatus(user.getId(), book.getId(), Status.BORROWED);
        if (exists) {
            throw new BookAlreadyBorrowedException("You have already borrowed this book.");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        LocalDateTime borrowed = LocalDateTime.now();
        Borrow borrow = borrowMapper.toEntity(borrowRequest);

        borrow.setUserId(user.getId());
        borrow.setBorrowAt(borrowed);
        borrow.setDueDate(LocalDate.now().plusDays(14));
        borrow.setStatus(Status.BORROWED);

        borrowRepository.save(borrow);
        LibraryEvent event = LibraryEvent.builder()
                .userId(user.getId())
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .eventType(EventType.BOOK_BORROWED)
                .eventTime(borrowed)
                .build();
        publisher.publish(RabbitMQConfig.BORROW_CREATED,
                event);

        return borrowMapper.toResponse(borrow);
    }

    @Transactional
    public BorrowResponse retrunBook(Integer id, String token) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new BorrowNotFoundException("Borrow not found"));
        if (borrow.getStatus() == Status.RETURNED) {
            throw new BorrowNotFoundException("Book has already been returned");
        }

        Book book = bookRepository.findById(borrow.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
        LocalDateTime returnedAt = LocalDateTime.now();

        borrow.setReturnAt(returnedAt);
        borrow.setStatus(Status.RETURNED);
        borrowRepository.save(borrow);
        LibraryEvent event = LibraryEvent.builder()
                .userId(borrow.getUserId())
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .eventType(EventType.BOOK_RETURNED)
                .eventTime(returnedAt)
                .build();
        publisher.publish(RabbitMQConfig.BORROW_RETURNED,
                event);

        return borrowMapper.toResponse(borrow);
    }

    public List<BorrowResponse> getMyBorrow(String token) {

        UserResponse user = userClient.getCUrrentUser(token);
        List<Borrow> borrows = borrowRepository.findByUserId(user.getId());
        return borrows.stream()
                .map(borrowMapper::toResponse)
                .toList();
    }

    public Page<BorrowResponse> getAllBorrows(String token, Pageable pageable) {
        checkAdmin(token);
        return borrowRepository.findAll(pageable)
                .map(borrowMapper::toResponse);
    }

    public List<BorrowResponse> getOverDueBorrows(String token) {
checkAdmin(token);
        List<Borrow> borrows = borrowRepository.findAll();

        return borrows.stream()
                .filter(borrow ->
                        borrow.getStatus() == Status.BORROWED &&
                                borrow.getDueDate().isBefore(LocalDate.now()))
                .map(borrowMapper::toResponse)
                .toList();
    }
}
