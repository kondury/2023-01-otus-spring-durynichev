package com.github.kondury.library.service.coverter;

import com.github.kondury.library.domain.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreConverterImpl implements GenreConverter {

    @Override
    public String convert(Genre genre) {
        return "Genre(id=%s, name=\"%s\")".formatted(genre.getId(), genre.getName());
    }
}
