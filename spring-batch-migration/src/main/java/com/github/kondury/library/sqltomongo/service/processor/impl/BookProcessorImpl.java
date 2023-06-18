package com.github.kondury.library.sqltomongo.service.processor.impl;

import com.github.kondury.library.mongo.model.BookDocument;
import com.github.kondury.library.sql.model.Book;
import com.github.kondury.library.sqltomongo.service.cache.AuthorKeyValueCache;
import com.github.kondury.library.sqltomongo.service.cache.GenreKeyValueCache;
import com.github.kondury.library.sqltomongo.service.processor.BookProcessor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookProcessorImpl implements BookProcessor {

    private final AuthorKeyValueCache authorCache;
    private final GenreKeyValueCache genreCache;

    @Override
    public BookDocument process(Book item) {
        var author = authorCache.get(item.getAuthor().getId());
        var genre = genreCache.get(item.getGenre().getId());
        return new BookDocument(ObjectId.get().toHexString(), item.getTitle(), author, genre);
    }
}
