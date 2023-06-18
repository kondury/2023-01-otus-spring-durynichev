package com.github.kondury.library.mongo.repository;

import com.github.kondury.library.mongo.model.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentDocumentRepository extends MongoRepository<CommentDocument, String> {
}