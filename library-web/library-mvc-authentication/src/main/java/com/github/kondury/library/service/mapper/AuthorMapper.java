package com.github.kondury.library.service.mapper;

import com.github.kondury.library.domain.Author;
import com.github.kondury.library.dto.AuthorDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto authorToAuthorDto(Author Author);
}
