package org.example.exception;

public class AuthorAlreadyExistsException extends RuntimeException{
    public AuthorAlreadyExistsException(String message) {
        super(message);
    }
}
