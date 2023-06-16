package com.github.kondury.library.sqltomongo.service.listener;

import com.github.kondury.library.mongo.model.GenreDocument;
import com.github.kondury.library.sql.model.Genre;
import org.springframework.batch.core.ItemProcessListener;

public interface GenreItemProcessListener extends ItemProcessListener<Genre, GenreDocument> {
}
