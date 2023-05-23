package com.github.kondury.library.repository;

import com.github.kondury.library.domain.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String>, CommentRepositoryCustom {
    Mono<Void> deleteByBookId(String bookId);
}