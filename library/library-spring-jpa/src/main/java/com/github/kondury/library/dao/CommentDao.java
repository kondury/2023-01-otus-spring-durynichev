package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDao {

    List<Comment> findByBookId(Long bookId);
    Optional<Comment> findByIdCompleted(Long id);
    Comment save(Comment comment);
    boolean deleteById(Long id);
}
