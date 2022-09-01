package org.example.exception;

public class GenreAlreadyExistsException extends RuntimeException {

    public GenreAlreadyExistsException(String message) {
        super(message);
    }
}
