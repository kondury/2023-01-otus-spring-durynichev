package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Book save(Book book);
    List<Book> findAllCompleted();
    Optional<Book> findByIdCompleted(Long id);
    boolean deleteById(Long id);

    Book getReferenceById(Long id);
}
