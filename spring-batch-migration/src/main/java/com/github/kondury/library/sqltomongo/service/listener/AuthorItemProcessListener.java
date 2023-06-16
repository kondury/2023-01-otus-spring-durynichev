package com.github.kondury.library.sqltomongo.service.listener;

import com.github.kondury.library.mongo.model.AuthorDocument;
import com.github.kondury.library.sql.model.Author;
import org.springframework.batch.core.ItemProcessListener;

public interface AuthorItemProcessListener extends ItemProcessListener<Author, AuthorDocument> {
}
