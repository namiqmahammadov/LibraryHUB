package com.namiq.msbook.service

import com.namiq.msbook.dao.entity.Book
import com.namiq.msbook.dao.entity.Borrow
import com.namiq.msbook.dao.repository.BookRepository
import com.namiq.msbook.dao.repository.BorrowRepository
import com.namiq.msbook.dto.request.BorrowRequest
import com.namiq.msbook.dto.response.BorrowResponse
import com.namiq.msbook.dto.response.UserResponse
import com.namiq.msbook.enums.Status
import com.namiq.msbook.exception.BookAlreadyBorrowedException
import com.namiq.msbook.exception.BookNotAvailableException
import com.namiq.msbook.mapper.BorrowMapper
import com.namiq.msbook.publisher.LibraryEventPublisher
import spock.lang.Specification

class BorrowServiceSpec extends Specification {
    BorrowRepository borrowRepository=Mock()
    BookRepository bookRepository=Mock()
    UserClient userClient=Mock()
    BorrowMapper borrowMapper=Mock()
    LibraryEventPublisher publisher=Mock()
    BorrowService borrowService=new BorrowService(borrowRepository,bookRepository,userClient,borrowMapper,publisher)
    def "should throw exception when book has no available copies"(){
        given :

        def request =new BorrowRequest()
        request.setBookId(1)
        def book=new Book()
        book.setId(1)
        book.setAvailableCopies(0)
        1* bookRepository.findById(1)>>Optional.of(book)
        when:
        borrowService.borrowBook("token",request)
        then:
        def exception = thrown(BookNotAvailableException)
        exception.message == "No available copies for this book"
    }
    def "should throw exception when book has already borrowed"(){
        given:
        def token="Bearer token"
        def request=new BorrowRequest()
        request.setBookId(1)
        def borrow=new Borrow()
        borrow.setStatus(Status.BORROWED)
        def user=new UserResponse()
        user.setId(1)
        def book=new Book()
        book.setId(1)
        book.setAvailableCopies(1)
        and:
        1* bookRepository.findById(1)>>Optional.of(book)
        1*userClient.getCUrrentUser(token)>>user
        1*borrowRepository.existsByUserIdAndBookIdAndStatus(1,1,Status.BORROWED)>>true
        when:
        borrowService.borrowBook(token,request)
        then:
        def exception=thrown(BookAlreadyBorrowedException)
        exception.message=="You have already borrowed this book."

    }
    def "should creat borrow successfully"(){
        given:
        def token="Bearer token"

        def request=new BorrowRequest()
        request.setBookId(1)
        def user=new UserResponse()
        user.setId(1)
        def book=new Book()
        book.setId(1)
        book.setAvailableCopies(2)
        def borrow=new Borrow()
        borrow.setStatus(Status.BORROWED)

        and:
        1* bookRepository.findById(1)>>Optional.of(book)
        1*userClient.getCUrrentUser(token)>>user
        1*borrowRepository.existsByUserIdAndBookIdAndStatus(1,1,Status.BORROWED)>>false
        1*bookRepository.save({it.availableCopies==1})
        1* borrowMapper.toEntity(request)>>borrow
        1*borrowRepository.save(borrow)
        1*borrowMapper.toResponse(borrow)>>new BorrowResponse()
        when:
        borrowService.borrowBook(token,request)
        then:
       noExceptionThrown()

    }
}
