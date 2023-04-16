package com.github.kondury.library.service;

import com.github.kondury.library.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> insert(String bookId, String text);
    List<Comment> findByBookId(String bookId);
    Optional<Comment> findById(String id);
    Optional<Comment> update(String commentId, String bookId, String text);
    void deleteById(String id);
}
