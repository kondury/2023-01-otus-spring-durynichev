package com.github.kondury.quiz.utils;


public interface Manager<K, T> {
    T get(K key);
}
