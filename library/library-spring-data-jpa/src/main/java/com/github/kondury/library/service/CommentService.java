package com.github.kondury.library.service;

import com.github.kondury.library.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> insert(long bookId, String text);
    List<Comment> findByBookId(long bookId);
    Optional<Comment> findById(long id);
    Optional<Comment> update(long commentId, long bookId, String text);
    void deleteById(long id);
}
