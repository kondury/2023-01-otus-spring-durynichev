package com.github.kondury.library.service.coverter;

import com.github.kondury.library.domain.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreConverterImpl implements GenreConverter {

    public static final String UNKNOWN_GENRE = "<UNKNOWN GENRE>";

    @Override
    public String convert(Genre genre) {
        return genre != null
                ? "Genre(id=%s, name=\"%s\")".formatted(genre.getId(), genre.getName())
                : UNKNOWN_GENRE;

    }
}
