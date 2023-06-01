package com.github.kondury.library.service;

import com.github.kondury.library.service.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    CommentDto insert(long bookId, String text);

    List<CommentDto> findByBookId(long bookId);

    Optional<CommentDto> findById(long id);

    CommentDto update(long commentId, long bookId, String text);

    void deleteById(long id);
}
