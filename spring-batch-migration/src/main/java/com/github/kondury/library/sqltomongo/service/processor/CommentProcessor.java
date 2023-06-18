package com.github.kondury.library.sqltomongo.service.processor;

import com.github.kondury.library.mongo.model.CommentDocument;
import com.github.kondury.library.sql.model.Comment;
import org.springframework.batch.item.ItemProcessor;

public interface CommentProcessor extends ItemProcessor<Comment, CommentDocument> {
}
