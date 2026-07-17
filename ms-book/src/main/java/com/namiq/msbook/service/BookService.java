package com.namiq.msbook.service;

import com.namiq.msbook.config.RabbitMQConfig;
import com.namiq.msbook.dao.entity.Book;
import com.namiq.msbook.dao.entity.Category;
import com.namiq.msbook.dao.repository.BookRepository;
import com.namiq.msbook.dao.repository.CategoryRepository;
import com.namiq.msbook.dto.request.BookRequest;
import com.namiq.msbook.dto.response.BookResponse;
import com.namiq.msbook.dto.response.UserResponse;
import com.namiq.msbook.enums.EventType;
import com.namiq.msbook.enums.Role;
import com.namiq.msbook.event.LibraryEvent;
import com.namiq.msbook.exception.*;
import com.namiq.msbook.mapper.BookMapper;
import com.namiq.msbook.publisher.LibraryEventPublisher;
import com.namiq.msbook.specification.BookSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final LibraryEventPublisher publisher;
    private final UserClient userClient;

    private void checkAdmin(String token) {
        UserResponse user = userClient.getCUrrentUser(token);

        if (user.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedOperationException("Only admins can perform this operation.");
        }
    }

    public void createBook(String token, BookRequest request) {
        checkAdmin(token);
        if (request.getTotalCopies() == null || request.getTotalCopies() <= 0) {
            throw new InvalidBookCopyException("Total copies must be greater than zero");
        }
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new IsbnAlreadyExistsException("ISBN already exists");

        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        Book book = bookMapper.toEntity(request, category);


        bookRepository.save(book);
        LibraryEvent event = LibraryEvent.builder()
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .eventType(EventType.BOOK_CREATED)
                .eventTime(LocalDateTime.now())
                .build();
        publisher.publish(
                RabbitMQConfig.BOOK_CREATED,
                event
        );
    }
    @Cacheable(
            value = "books",
            key = "'category=' + #categoryId + ':author=' + #author + ':page=' + #pageable.pageNumber + ':size=' + #pageable.pageSize + ':sort=' + #pageable.sort"
    )
    public Page<BookResponse> getAllBooks(
            Integer categoryId,
            String author,
            Pageable pageable) {
        Specification<Book> specification=
                BookSpecification.filter(categoryId,author);

        return bookRepository.findAll(specification, pageable)
                .map(bookMapper::toResponse);
    }

    @Cacheable(value = "books", key = "#id")
    public BookResponse getBookById(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new BookNotFoundException("Book not found"));
        return bookMapper.toResponse(book);

    }

    public List<BookResponse> search(String keyword) {
        return bookRepository.searchBooks(keyword).stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    @CacheEvict(value = "books", allEntries = true)
    public BookResponse editBooks(String token, Integer id, @Valid BookRequest bookRequest) {
        checkAdmin(token);
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new BookNotFoundException("Book not found"));

        Category category = categoryRepository.findById(bookRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        int borrowedCopies = book.getTotalCopies() - book.getAvailableCopies();
        if (bookRequest.getTotalCopies() < borrowedCopies) {
            throw new InvalidBookCopyException("Total copies cannot be less than borrowed copies.");
        }
        bookMapper.updateEntity(book, bookRequest, category);
        book.setUpdatedAt(LocalDateTime.now());
        Book updateBook = bookRepository.save(book);
        return bookMapper.toResponse(updateBook);
    }

    @CacheEvict(value = "books", allEntries = true)
    public void deleteById(String token, Integer id) {
        checkAdmin(token);
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new BookNotFoundException("Book not found"));
        if (!book.getIsActive()) {
            throw new BookNotFoundException("Book is already block");
        }
        book.setIsActive(false);
        bookRepository.save(book);
    }

}