package com.github.kondury.library.sqltomongo.service.listener.impl;

import com.github.kondury.library.mongo.model.AuthorDocument;
import com.github.kondury.library.sql.model.Author;
import com.github.kondury.library.sqltomongo.service.cache.AuthorKeyValueCache;
import com.github.kondury.library.sqltomongo.service.listener.AuthorItemProcessListener;
import org.springframework.stereotype.Component;

@Component
public class CachingAuthorItemProcessListener extends CachingItemProcessListener<Long, Author, AuthorDocument>
        implements AuthorItemProcessListener {

    public CachingAuthorItemProcessListener(AuthorKeyValueCache authorCache) {
        super(authorCache, Author::getId);
    }
}
