package com.namiq.msuser.listener;

import com.namiq.msuser.config.RabbitMQConfig;
import com.namiq.msuser.dao.entity.BorrowHistory;
import com.namiq.msuser.dao.repository.BorrowHistoryRepository;
import com.namiq.msuser.enums.Status;
import com.namiq.msuser.event.LibraryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BorrowEventListener {

    private final BorrowHistoryRepository borrowHistoryRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receive(LibraryEvent event) {


        switch (event.getEventType()) {

            case BOOK_CREATED -> {

            }

            case BOOK_BORROWED -> {

                BorrowHistory history = BorrowHistory.builder()
                        .userId(event.getUserId())
                        .bookId(event.getBookId())
                        .bookTitle(event.getBookTitle())
                        .borrowAt(event.getEventTime())
                        .status(Status.BORROWED)
                        .build();

                borrowHistoryRepository.save(history);

            }

            case BOOK_RETURNED -> {

                BorrowHistory history =
                        borrowHistoryRepository
                                .findTopByUserIdAndBookIdOrderByBorrowAtDesc(
                                        event.getUserId(),
                                        event.getBookId()
                                )
                                .orElseThrow();

                history.setReturnAt(event.getEventTime());
                history.setStatus(Status.RETURNED);

                borrowHistoryRepository.save(history);

            }
            case BOOK_OVERDUE -> {
                BorrowHistory history=
                        borrowHistoryRepository.findTopByUserIdAndBookIdOrderByBorrowAtDesc(
                                event.getUserId(),
                                event.getBookId()
                        )
                                .orElseThrow();
                if (history.getStatus() != Status.OVERDUE) {
                    history.setStatus(Status.OVERDUE);
                    borrowHistoryRepository.save(history);
                }
            }
        }

    }

}