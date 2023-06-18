package com.github.kondury.library.sqltomongo.service.listener.impl;

import com.github.kondury.library.mongo.model.BookDocument;
import com.github.kondury.library.sql.model.Book;
import com.github.kondury.library.sqltomongo.service.cache.BookKeyValueCache;
import com.github.kondury.library.sqltomongo.service.listener.BookItemProcessListener;
import org.springframework.stereotype.Component;

@Component
public class CachingBookItemProcessListener extends CachingItemProcessListener<Long, Book, BookDocument>
        implements BookItemProcessListener {

    public CachingBookItemProcessListener(BookKeyValueCache bookCache) {
        super(bookCache, Book::getId);
    }
}
