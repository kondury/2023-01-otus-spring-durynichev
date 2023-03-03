package com.github.kondury.quiz.service;


public interface MessageService {
    String getMessage(String messageCode, Object[] args);
    String getMessage(String messageCode);
}
