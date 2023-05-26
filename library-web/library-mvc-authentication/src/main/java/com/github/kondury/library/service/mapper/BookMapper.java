package com.github.kondury.library.service.mapper;

import com.github.kondury.library.domain.Book;
import com.github.kondury.library.dto.BookDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {AuthorMapper.class, GenreMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookMapper {
    BookDto bookToBookDto(Book book);
}
