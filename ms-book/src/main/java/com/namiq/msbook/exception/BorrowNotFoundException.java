package com.namiq.msbook.exception;

public class BorrowNotFoundException extends RuntimeException {
    public BorrowNotFoundException(String m) {
        super(m);
    }
}
