package com.github.kondury.library.repository;

import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.ObjectOperators;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final ReactiveMongoOperations mongoTemplate;

    @Override
    public Flux<Comment> findCommentsByBookId(String bookId) {
        Aggregation aggregation = newAggregation(
                match(where("_id").is(new ObjectId(bookId))),
                addFields().addField("bookId").withValue(bookId).build(),
                lookup("comments", "bookId", "bookId", "comments"),
                project()
                        .and("book").nested(Fields.fields("_id", "title", "author", "genre"))
                        .andInclude("comments")
                        .andExclude("_id"),
                unwind("comments"),
                replaceRoot().withValueOf(ObjectOperators.valueOf("comments").mergeWith(ROOT)),
                project().andExclude("comments")
        );
        return mongoTemplate.aggregate(aggregation, Book.class, Comment.class);
    }
}
