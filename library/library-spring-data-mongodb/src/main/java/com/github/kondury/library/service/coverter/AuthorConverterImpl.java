package com.github.kondury.library.service.coverter;

import com.github.kondury.library.domain.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorConverterImpl implements AuthorConverter {

    public static final String UNKNOWN_AUTHOR = "<UNKNOWN AUTHOR>";

    @Override
    public String convert(Author author) {
        return author != null
                ? "Author(id=%s, name=\"%s\")".formatted(author.getId(), author.getName())
                : UNKNOWN_AUTHOR;
    }
}
