package com.namiq.msbook.mapper;

import com.namiq.msbook.dao.entity.Borrow;
import com.namiq.msbook.dto.request.BorrowRequest;
import com.namiq.msbook.dto.response.BorrowResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-17T11:36:30+0400",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-java-compiler-worker-9.5.1.jar, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class BorrowMapperImpl implements BorrowMapper {

    @Override
    public Borrow toEntity(BorrowRequest request) {
        if ( request == null ) {
            return null;
        }

        Borrow.BorrowBuilder borrow = Borrow.builder();

        borrow.bookId( request.getBookId() );

        return borrow.build();
    }

    @Override
    public BorrowResponse toResponse(Borrow borrow) {
        if ( borrow == null ) {
            return null;
        }

        BorrowResponse.BorrowResponseBuilder borrowResponse = BorrowResponse.builder();

        borrowResponse.id( borrow.getId() );
        borrowResponse.bookId( borrow.getBookId() );
        borrowResponse.userId( borrow.getUserId() );
        borrowResponse.borrowAt( borrow.getBorrowAt() );
        borrowResponse.dueDate( borrow.getDueDate() );
        borrowResponse.returnAt( borrow.getReturnAt() );
        borrowResponse.status( borrow.getStatus() );

        return borrowResponse.build();
    }
}
