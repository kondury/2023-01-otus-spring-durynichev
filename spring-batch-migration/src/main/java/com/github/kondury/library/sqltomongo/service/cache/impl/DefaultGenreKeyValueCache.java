package com.github.kondury.library.sqltomongo.service.cache.impl;

import com.github.kondury.library.mongo.model.GenreDocument;
import com.github.kondury.library.sqltomongo.service.cache.AbstractKeyValueCache;
import com.github.kondury.library.sqltomongo.service.cache.GenreKeyValueCache;
import org.springframework.stereotype.Component;

@Component
public class DefaultGenreKeyValueCache extends AbstractKeyValueCache<Long, GenreDocument> implements GenreKeyValueCache {
}
