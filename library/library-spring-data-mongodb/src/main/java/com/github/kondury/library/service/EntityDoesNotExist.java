package com.github.kondury.library.service;

public class EntityDoesNotExist extends RuntimeException {
    public EntityDoesNotExist(String message) {
        super(message);
    }
}
