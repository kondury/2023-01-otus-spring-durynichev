package com.github.kondury.library.service;

import com.github.kondury.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<Author> findAll();
    Optional<Author> findById(String id);
}
