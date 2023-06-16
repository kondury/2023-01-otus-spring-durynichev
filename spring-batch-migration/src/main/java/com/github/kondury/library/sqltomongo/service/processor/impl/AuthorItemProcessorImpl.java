package com.github.kondury.library.sqltomongo.service.processor.impl;

import com.github.kondury.library.mongo.model.AuthorDocument;
import com.github.kondury.library.sql.model.Author;
import com.github.kondury.library.sqltomongo.service.processor.AuthorItemProcessor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class AuthorItemProcessorImpl implements AuthorItemProcessor {

    @Override
    public AuthorDocument process(Author item) {
        return new AuthorDocument(ObjectId.get().toHexString(), item.getName());
    }
}
