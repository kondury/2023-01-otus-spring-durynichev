package com.github.kondury.library.sql.repository;

import com.github.kondury.library.sql.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}