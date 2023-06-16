package com.github.kondury.library.mongo.repository;

import com.github.kondury.library.mongo.model.GenreDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenreDocumentRepository extends MongoRepository<GenreDocument, String> {
}
