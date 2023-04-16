package com.github.kondury.library.service.coverter;

import com.github.kondury.library.domain.Genre;
import org.springframework.stereotype.Component;

import static com.github.kondury.library.domain.Genre.UNKNOWN_GENRE;

@Component
public class GenreConverterImpl implements GenreConverter {

    @Override
    public String convert(Genre genre) {
        return genre != null
                ? "Genre(id=%s, name=\"%s\")".formatted(genre.getId(), genre.getName())
                : UNKNOWN_GENRE;

    }
}
