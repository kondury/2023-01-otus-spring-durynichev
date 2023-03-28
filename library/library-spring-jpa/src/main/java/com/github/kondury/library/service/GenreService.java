package com.github.kondury.library.service;

import com.github.kondury.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<Genre> findAll();

    Optional<Genre> findById(long genreId);
}
