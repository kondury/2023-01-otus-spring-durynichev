package com.github.kondury.library.service.mapper;

import com.github.kondury.library.domain.Genre;
import com.github.kondury.library.service.dto.GenreDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto genreToGenreDto(Genre Genre);
}
