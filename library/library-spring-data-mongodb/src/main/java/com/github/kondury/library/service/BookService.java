package com.github.kondury.library.service;

import com.github.kondury.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> insert(String title, String authorId, String genreId);
    Optional<Book> update(String id, String title, String authorId, String genreId);
    List<Book> findAll();
    Optional<Book> findById(String id);
    void deleteById(String id);

}
