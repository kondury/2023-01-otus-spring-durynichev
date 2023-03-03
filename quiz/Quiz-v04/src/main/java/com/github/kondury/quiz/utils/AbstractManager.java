package com.github.kondury.quiz.utils;

import java.util.Map;

public class AbstractManager<K, T> implements Manager<K, T> {
    private final Map<K, T> strategies;

    protected AbstractManager(Map<K, T> strategies) {
        this.strategies = strategies;
    }

    public T get(K key) {
        return strategies.get(key);
    }
}
