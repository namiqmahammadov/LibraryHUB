package com.namiq.msbook.service

import com.namiq.msbook.config.RabbitMQConfig
import com.namiq.msbook.dao.entity.Borrow
import com.namiq.msbook.dao.repository.BorrowRepository
import com.namiq.msbook.enums.Status
import com.namiq.msbook.publisher.LibraryEventPublisher
import com.namiq.msbook.scheduler.BorrowScheduler
import spock.lang.Specification

import java.time.LocalDate

class SchedulerSpec extends Specification {
    BorrowRepository borrowRepository = Mock()
    LibraryEventPublisher publisher = Mock()

    BorrowScheduler borrowScheduler =
            new BorrowScheduler(borrowRepository, publisher)

    def "should mark overdue books successfully"() {
        given:
        def borrow=new Borrow()
        borrow.setId(1)
        borrow.setUserId(2)
        borrow.setBookId(3)
        borrow.setStatus(Status.BORROWED)
        borrow.setDueDate(LocalDate.now().minusDays(1))
        def borrows=[borrow]
        1*borrowRepository.findByStatusAndDueDateBefore(Status.BORROWED,LocalDate.now())>>borrows
        when:
        borrowScheduler.updateOverdueBooks()
        then:
        borrow.status==Status.OVERDUE
        1 * borrowRepository.saveAll({
            it[0].status == Status.OVERDUE
        })
        1*  publisher.publish(RabbitMQConfig.BORROW_OVERDUE, _)
    }
}
