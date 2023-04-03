package com.github.kondury.library.service.coverter;

import com.github.kondury.library.domain.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorConverterImpl implements AuthorConverter {

    @Override
    public String convert(Author author) {
        return "Author(id=%s, name=\"%s\")".formatted(author.getId(), author.getName());
    }
}
