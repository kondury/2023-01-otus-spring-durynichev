package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    List<Genre> findAll();

    Optional<Genre> findById(Long id);
}
