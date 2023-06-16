package com.github.kondury.library.mongo.repository;

import com.github.kondury.library.mongo.model.AuthorDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorDocumentRepository extends MongoRepository<AuthorDocument, String> {
}
