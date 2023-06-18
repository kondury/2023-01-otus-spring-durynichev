package com.github.kondury.library.sqltomongo.service.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractKeyValueCache<K, V> implements KeyValueCache<K, V> {

    private final Map<K, V> cache = new ConcurrentHashMap<>();

    @Override
    public V put(K key, V value) {
        return cache.put(key, value);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }
}
