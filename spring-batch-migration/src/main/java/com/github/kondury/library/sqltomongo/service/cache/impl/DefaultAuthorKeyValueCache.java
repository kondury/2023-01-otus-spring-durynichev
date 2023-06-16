package com.github.kondury.library.sqltomongo.service.cache.impl;

import com.github.kondury.library.mongo.model.AuthorDocument;
import com.github.kondury.library.sqltomongo.service.cache.AbstractKeyValueCache;
import com.github.kondury.library.sqltomongo.service.cache.AuthorKeyValueCache;
import org.springframework.stereotype.Component;

@Component
public class DefaultAuthorKeyValueCache extends AbstractKeyValueCache<Long, AuthorDocument> implements AuthorKeyValueCache {
}
