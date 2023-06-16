package com.github.kondury.library.sqltomongo.service.processor;

import com.github.kondury.library.mongo.model.BookDocument;
import com.github.kondury.library.sql.model.Book;
import org.springframework.batch.item.ItemProcessor;

public interface BookProcessor extends ItemProcessor<Book, BookDocument> {
}
