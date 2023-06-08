package com.github.kondury.library.repository;

import com.github.kondury.library.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(attributePaths = {"author", "genre"})
    List<Book> findAll();

    @Override
    @EntityGraph(attributePaths = {"author", "genre"})
    Optional<Book> findById(Long id);
}