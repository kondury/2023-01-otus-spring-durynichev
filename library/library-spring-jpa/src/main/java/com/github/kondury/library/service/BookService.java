package com.github.kondury.library.service;

import com.github.kondury.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> insert(String title, long authorId, long genreId);
    Optional<Book> update(long id, String title, long authorId, long genreId);

    List<Book> findAll();
    Optional<Book> findById(long id);
    boolean deleteById(long id);

}
