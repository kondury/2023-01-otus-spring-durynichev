package com.github.kondury.library.repository;

import com.github.kondury.library.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void removeGenreById(String genreId) {
        val query = Query.query(Criteria.where("$id").is(new ObjectId(genreId)));
        val update = new Update().unset("genre");
        mongoTemplate.updateMulti(query, update, Book.class);
    }
}
