package com.namiq.msbook.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(String m) {
        super(m);
    }
}
