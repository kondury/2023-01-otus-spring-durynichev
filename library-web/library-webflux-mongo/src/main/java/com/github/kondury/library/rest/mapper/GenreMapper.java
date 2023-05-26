package com.github.kondury.library.rest.mapper;

import com.github.kondury.library.domain.Genre;
import com.github.kondury.library.rest.dto.GenreDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto genreToGenreDto(Genre Genre);
}
