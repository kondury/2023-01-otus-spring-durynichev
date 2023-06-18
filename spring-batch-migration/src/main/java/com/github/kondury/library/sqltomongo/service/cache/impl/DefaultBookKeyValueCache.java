package com.github.kondury.library.sqltomongo.service.cache.impl;

import com.github.kondury.library.mongo.model.BookDocument;
import com.github.kondury.library.sqltomongo.service.cache.AbstractKeyValueCache;
import com.github.kondury.library.sqltomongo.service.cache.BookKeyValueCache;
import org.springframework.stereotype.Component;

@Component
public class DefaultBookKeyValueCache extends AbstractKeyValueCache<Long, BookDocument> implements BookKeyValueCache {
}
