package com.namiq.msbook.service

import com.namiq.msbook.dao.entity.Book
import com.namiq.msbook.dao.entity.Category
import com.namiq.msbook.dao.repository.BookRepository
import com.namiq.msbook.dao.repository.CategoryRepository
import com.namiq.msbook.dto.request.BookRequest
import com.namiq.msbook.exception.InvalidBookCopyException
import com.namiq.msbook.exception.IsbnAlreadyExistsException
import com.namiq.msbook.mapper.BookMapper
import com.namiq.msbook.publisher.LibraryEventPublisher
import spock.lang.Specification

class BookServiceSpec extends Specification {
    BookRepository bookRepository = Mock()
    CategoryRepository categoryRepository = Mock()
    BookMapper bookMapper = Mock()
    LibraryEventPublisher publisher=Mock()
    BookService bookService = new BookService(bookRepository, categoryRepository, bookMapper,publisher)

    def "should create book successfully"() {
        given:
        def request = new BookRequest()

        request.setCategoryId(1)
        request.setIsbn("12345")
        def category = new Category()
        category.setId(1)
        def book = new Book()
        book.setIsbn("12345")
        book.setCategory(category)

        when:
        bookService.createBook(request)
        then:
        1* bookRepository.existsByIsbn("12345")>>false
        1 * categoryRepository.findById(1)>>Optional.of(category)
        1 * bookMapper.toEntity(request, category)>>book
        1 * bookRepository.save(book)

    }
    def"should throw exception when isbn  already exists"(){
        given:
        def request = new BookRequest()
        request.setIsbn("12345")

        and:
        1* bookRepository.existsByIsbn("12345")>>true
        when:
        bookService.createBook(request)
        then:
        def exception = thrown(IsbnAlreadyExistsException)
        exception.message == "ISBN already exists"
    }
    def "should throw exception when total copies is zero or null"(){
        given :
        def request= new BookRequest()
        request.setTotalCopies(-1)
        when:
        bookService.createBook(request)
        then:
        def exception=thrown(InvalidBookCopyException)
        exception.message== "Total copies must be greater than zero"
    }
}
