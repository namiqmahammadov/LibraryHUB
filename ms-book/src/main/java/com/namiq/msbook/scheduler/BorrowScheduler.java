package com.namiq.msbook.scheduler;

import com.namiq.msbook.config.RabbitMQConfig;
import com.namiq.msbook.dao.entity.Borrow;
import com.namiq.msbook.dao.repository.BorrowRepository;
import com.namiq.msbook.enums.EventType;
import com.namiq.msbook.enums.Status;
import com.namiq.msbook.event.LibraryEvent;
import com.namiq.msbook.publisher.LibraryEventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BorrowScheduler {
    private  final BorrowRepository borrowRepository;
    private final LibraryEventPublisher publisher;
@Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateOverdueBooks(){
        List<Borrow> borrows=borrowRepository.findByStatusAndDueDateBefore(Status.BORROWED, LocalDate.now());
        LocalDateTime eventTime=LocalDateTime.now();
        for(Borrow borrow:borrows){
            borrow.setStatus(Status.OVERDUE);
            LibraryEvent  event=LibraryEvent.builder()
                    .userId(borrow.getUserId())
                    .bookId(borrow.getBookId())
                    .eventType(EventType.BOOK_OVERDUE)
                    .eventTime(eventTime)
                    .build();
            publisher.publish(RabbitMQConfig.BORROW_OVERDUE,event);
        }
        borrowRepository.saveAll(borrows);
    }
}
