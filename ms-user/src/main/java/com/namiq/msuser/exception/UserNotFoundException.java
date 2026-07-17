package com.namiq.msuser.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String m) {
        super(m);
    }
}
