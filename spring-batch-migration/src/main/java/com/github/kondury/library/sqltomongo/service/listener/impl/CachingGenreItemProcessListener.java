package com.github.kondury.library.sqltomongo.service.listener.impl;

import com.github.kondury.library.mongo.model.GenreDocument;
import com.github.kondury.library.sql.model.Genre;
import com.github.kondury.library.sqltomongo.service.cache.GenreKeyValueCache;
import com.github.kondury.library.sqltomongo.service.listener.GenreItemProcessListener;
import org.springframework.stereotype.Component;


@Component
public class CachingGenreItemProcessListener extends CachingItemProcessListener<Long, Genre, GenreDocument>
        implements GenreItemProcessListener {

    public CachingGenreItemProcessListener(GenreKeyValueCache genreCache) {
        super(genreCache, Genre::getId);
    }
}
