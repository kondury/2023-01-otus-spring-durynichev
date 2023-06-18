package com.github.kondury.library.mongo.repository;

import com.github.kondury.library.mongo.model.BookDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookDocumentRepository extends MongoRepository<BookDocument, String> {
}