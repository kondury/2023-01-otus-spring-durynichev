package com.github.kondury.quiz.utils;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class AbstractManager<K, T> implements Manager<K, T> {
    private final Map<K, T> strategies;

    public T get(K key) {
        return strategies.get(key);
    }
}
