package com.github.kondury.library.sqltomongo.service.processor;

import com.github.kondury.library.mongo.model.GenreDocument;
import com.github.kondury.library.sql.model.Genre;
import org.springframework.batch.item.ItemProcessor;

public interface GenreProcessor extends ItemProcessor<Genre, GenreDocument> {
}
