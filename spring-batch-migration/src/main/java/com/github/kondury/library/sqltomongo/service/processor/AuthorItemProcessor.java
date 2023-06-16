package com.github.kondury.library.sqltomongo.service.processor;

import com.github.kondury.library.mongo.model.AuthorDocument;
import com.github.kondury.library.sql.model.Author;
import org.springframework.batch.item.ItemProcessor;

public interface AuthorItemProcessor extends ItemProcessor<Author, AuthorDocument> {
}
