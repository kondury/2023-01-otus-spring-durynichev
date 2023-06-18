package com.github.kondury.library.sql.repository;

import com.github.kondury.library.sql.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}