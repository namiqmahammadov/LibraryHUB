package com.namiq.msuser.advice;

import com.namiq.msuser.dto.response.ErrorResponse;
import com.namiq.msuser.exception.EmailAlreadyExistsException;
import com.namiq.msuser.exception.InvalidRefreshTokenException;
import com.namiq.msuser.exception.UserAlreadyExistsException;
import com.namiq.msuser.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(UserNotFoundException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("USER_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handle(InvalidRefreshTokenException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("INVALID_REFRESH_TOKEN")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handle(UserAlreadyExistsException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("USER_ALREADY_EXISTS")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handle(EmailAlreadyExistsException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code("EMAIL_ALREADY_EXISTS")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
}