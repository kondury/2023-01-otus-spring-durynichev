package com.github.kondury.library.sqltomongo.service.listener.impl;

import com.github.kondury.library.sqltomongo.service.cache.KeyValueCache;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.lang.NonNull;

import java.util.function.Function;

public class CachingItemProcessListener<K, T, S> implements ItemProcessListener<T, S> {

    private final KeyValueCache<K, S> cache;
    private final Function<T, K> keyGetter;

    protected CachingItemProcessListener(KeyValueCache<K, S> cache, Function<T, K> keyGetter) {
        this.keyGetter = keyGetter;
        this.cache = cache;
    }

    @Override
    public void afterProcess(@NonNull T item, S result) {
        K key = keyGetter.apply(item);
        cache.put(key, result);
    }
}
