package com.github.kondury.library.rest.mapper;

import com.github.kondury.library.domain.Book;
import com.github.kondury.library.rest.dto.BookDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {AuthorMapper.class, GenreMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookMapper {
    BookDto bookToBookDto(Book book);
}
