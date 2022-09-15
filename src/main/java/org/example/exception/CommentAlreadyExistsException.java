package org.example.exception;

public class CommentAlreadyExistsException extends RuntimeException{

    public CommentAlreadyExistsException(String message) {
        super(message);
    }
}
