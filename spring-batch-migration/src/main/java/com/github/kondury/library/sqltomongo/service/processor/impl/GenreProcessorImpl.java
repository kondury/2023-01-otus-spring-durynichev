package com.github.kondury.library.sqltomongo.service.processor.impl;

import com.github.kondury.library.mongo.model.GenreDocument;
import com.github.kondury.library.sql.model.Genre;
import com.github.kondury.library.sqltomongo.service.processor.GenreProcessor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class GenreProcessorImpl implements GenreProcessor {

    @Override
    public GenreDocument process(Genre item) {
        return new GenreDocument(ObjectId.get().toHexString(), item.getName());
    }
}
