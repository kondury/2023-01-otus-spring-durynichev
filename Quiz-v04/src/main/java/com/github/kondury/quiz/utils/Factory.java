package com.github.kondury.quiz.utils;


public interface Factory<T> {
    T createFrom(String source);
}
