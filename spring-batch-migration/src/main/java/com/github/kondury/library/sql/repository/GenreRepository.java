package com.github.kondury.library.sql.repository;

import com.github.kondury.library.sql.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
