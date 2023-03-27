package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Book insert(Book book);
    Book update(Book book);
    List<Book> findAll();
    Optional<Book> findById(long id);
    boolean deleteById(long id);
}
