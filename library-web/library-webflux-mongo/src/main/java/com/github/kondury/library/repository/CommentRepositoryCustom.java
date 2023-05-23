package com.github.kondury.library.repository;

import com.github.kondury.library.domain.Comment;
import reactor.core.publisher.Flux;

public interface CommentRepositoryCustom {

    Flux<Comment> findCommentsByBookId(String bookId);
}
