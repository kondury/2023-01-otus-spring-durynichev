package com.github.kondury.library.sqltomongo.service.processor.impl;

import com.github.kondury.library.mongo.model.CommentDocument;
import com.github.kondury.library.sql.model.Comment;
import com.github.kondury.library.sqltomongo.service.cache.BookKeyValueCache;
import com.github.kondury.library.sqltomongo.service.processor.CommentProcessor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentProcessorImpl implements CommentProcessor {

    private final BookKeyValueCache bookCache;

    @Override
    public CommentDocument process(Comment item) {
        var book = bookCache.get(item.getBook().getId());
        return new CommentDocument(ObjectId.get().toHexString(), item.getText(), book);
    }

}
