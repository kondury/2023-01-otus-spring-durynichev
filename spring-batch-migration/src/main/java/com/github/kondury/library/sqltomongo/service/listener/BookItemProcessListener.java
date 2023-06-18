package com.github.kondury.library.sqltomongo.service.listener;

import com.github.kondury.library.mongo.model.BookDocument;
import com.github.kondury.library.sql.model.Book;
import org.springframework.batch.core.ItemProcessListener;

public interface BookItemProcessListener extends ItemProcessListener<Book, BookDocument> {
}
