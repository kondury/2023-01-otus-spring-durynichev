package com.github.kondury.library.service;

import com.github.kondury.library.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<GenreDto> findAll();

    Optional<GenreDto> findById(long genreId);
}
