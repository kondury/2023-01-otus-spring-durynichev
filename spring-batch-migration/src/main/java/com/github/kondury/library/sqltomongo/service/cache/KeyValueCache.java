package com.github.kondury.library.sqltomongo.service.cache;

public interface KeyValueCache<K, V> {
    V put(K key, V value);
    V get(K key);
}
