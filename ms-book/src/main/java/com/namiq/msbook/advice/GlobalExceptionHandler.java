package com.namiq.msbook.advice;

import com.namiq.msbook.dto.response.ErrorResponse;
import com.namiq.msbook.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(CategoryNotFoundException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("CATEGORY_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(List.of())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CategoryNameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handle(CategoryNameAlreadyExistsException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("CATEGORY_NAME_ALREADY_EXISTS")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(List.of())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ErrorResponse response = ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message("Validation failed")
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(List.of())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(BookNotFoundException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("BOOK_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(List.of())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidBookCopyException.class)
    public  ResponseEntity<ErrorResponse> handle(InvalidBookCopyException ex){
        ErrorResponse response=ErrorResponse.builder()
                .code("INVALID_TOTAL_COPIES")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(List.of())
                .build();
        return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BorrowNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(BorrowNotFoundException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("BORROW_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(List.of())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BookAlreadyBorrowedException.class)
    public ResponseEntity<ErrorResponse> handle(BookAlreadyBorrowedException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("BOOK_ALREADY_BORROWED")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(List.of())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handle(BookNotAvailableException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("BOOK_NOT_AVAILABLE")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(List.of())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(IsbnAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handle(IsbnAlreadyExistsException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("ISBN_ALREADY_EXISTS")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(List.of())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<ErrorResponse> handle(UnauthorizedOperationException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("ACCESS_DENIED")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .errors(List.of())
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}