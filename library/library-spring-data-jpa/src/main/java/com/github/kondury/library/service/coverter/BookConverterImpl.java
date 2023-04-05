package com.github.kondury.library.service.coverter;

import com.github.kondury.library.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookConverterImpl implements BookConverter {

    private final AuthorConverter authorConverter;
    private final GenreConverter genreConverter;

    @Override
    public String convert(Book book) {
        return "Book(id=%s, title=\"%s\", author=%s, genre=%s)"
                .formatted(book.getId(), book.getTitle(),
                        authorConverter.convert(book.getAuthor()),
                        genreConverter.convert(book.getGenre()));
    }
}
