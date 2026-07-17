package com.namiq.msbook.mapper;

import com.namiq.msbook.dao.entity.Book;
import com.namiq.msbook.dao.entity.Category;
import com.namiq.msbook.dto.request.BookRequest;
import com.namiq.msbook.dto.response.BookResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-17T11:36:30+0400",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-java-compiler-worker-9.5.1.jar, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public Book toEntity(BookRequest request, Category category) {
        if ( request == null && category == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        if ( request != null ) {
            book.description( request.getDescription() );
            book.title( request.getTitle() );
            book.author( request.getAuthor() );
            book.isbn( request.getIsbn() );
            book.totalCopies( request.getTotalCopies() );
            book.publishedYear( request.getPublishedYear() );
        }
        book.category( category );
        book.availableCopies( request.getTotalCopies() );
        book.isActive( true );

        return book.build();
    }

    @Override
    public void updateEntity(Book book, BookRequest request, Category category) {
        if ( request == null && category == null ) {
            return;
        }

        if ( request != null ) {
            if ( request.getDescription() != null ) {
                book.setDescription( request.getDescription() );
            }
            if ( request.getTitle() != null ) {
                book.setTitle( request.getTitle() );
            }
            if ( request.getAuthor() != null ) {
                book.setAuthor( request.getAuthor() );
            }
            if ( request.getIsbn() != null ) {
                book.setIsbn( request.getIsbn() );
            }
            if ( request.getTotalCopies() != null ) {
                book.setTotalCopies( request.getTotalCopies() );
            }
            if ( request.getPublishedYear() != null ) {
                book.setPublishedYear( request.getPublishedYear() );
            }
        }
        if ( category != null ) {
            book.setCategory( category );
        }
    }

    @Override
    public BookResponse toResponse(Book book) {
        if ( book == null ) {
            return null;
        }

        BookResponse.BookResponseBuilder bookResponse = BookResponse.builder();

        bookResponse.categoryId( bookCategoryId( book ) );
        bookResponse.id( book.getId() );
        bookResponse.title( book.getTitle() );
        bookResponse.author( book.getAuthor() );
        bookResponse.isbn( book.getIsbn() );
        bookResponse.description( book.getDescription() );
        bookResponse.totalCopies( book.getTotalCopies() );
        bookResponse.availableCopies( book.getAvailableCopies() );
        bookResponse.publishedYear( book.getPublishedYear() );
        bookResponse.isActive( book.getIsActive() );
        bookResponse.createdAt( book.getCreatedAt() );
        bookResponse.updatedAt( book.getUpdatedAt() );

        return bookResponse.build();
    }

    private Integer bookCategoryId(Book book) {
        Category category = book.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }
}
