package com.github.kondury.library.rest.mapper;

import com.github.kondury.library.domain.Author;
import com.github.kondury.library.rest.dto.AuthorDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto authorToAuthorDto(Author Author);
}
