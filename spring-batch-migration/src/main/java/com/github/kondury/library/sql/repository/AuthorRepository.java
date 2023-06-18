package com.github.kondury.library.sql.repository;

import com.github.kondury.library.sql.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
