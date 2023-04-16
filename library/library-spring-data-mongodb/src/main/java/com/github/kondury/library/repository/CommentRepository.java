package com.github.kondury.library.repository;

import com.github.kondury.library.domain.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByBookId(String bookId);

    void deleteCommentsByBookId(String bookId);

}