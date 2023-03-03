package com.github.kondury.quiz.io;

public class InputServiceException extends RuntimeException {
    public InputServiceException(String message, Exception e) {
        super(message, e);
    }
}
