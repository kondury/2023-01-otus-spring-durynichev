package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    List<Author> findAll();

    Optional<Author> findById(Long id);

    Author getReferenceById(Long id);
}
