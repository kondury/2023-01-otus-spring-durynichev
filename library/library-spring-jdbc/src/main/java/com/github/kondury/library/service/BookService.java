package com.github.kondury.library.service;

import com.github.kondury.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book insert(Book book);
    Book update(Book book);
    List<Book> findAll();
    Optional<Book> findById(long id);
    void deleteById(long id);

}
