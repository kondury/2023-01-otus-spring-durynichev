package com.github.kondury.library.sqltomongo.service.cache;

import com.github.kondury.library.mongo.model.BookDocument;

public interface BookKeyValueCache extends KeyValueCache<Long, BookDocument> {
}
