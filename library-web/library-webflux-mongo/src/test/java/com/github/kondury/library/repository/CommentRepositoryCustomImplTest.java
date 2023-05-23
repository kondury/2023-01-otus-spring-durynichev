package com.github.kondury.library.repository;

import com.github.kondury.library.domain.Author;
import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Comment;
import com.github.kondury.library.domain.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataMongoTest
class CommentRepositoryCustomImplTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReactiveMongoOperations mongoTemplate;

    @Test
    void findCommentsByBookId_shouldReturnAllBookCommentsWithCorrectFields() {

        Author frankHerbert = new Author("Frank Herbert");
        Genre scienceFiction = new Genre("Science Fiction");
        var title = "Dune";
        String commentText1 = "It's the real masterpiece!!! Must read!!!";
        String commentText2 = "As for me it was a little bit too lingering and somewhat boring";

        var expected = List.of(tuple(commentText1, title), tuple(commentText2, title));

        var setup = Mono.zip(mongoTemplate.insert(frankHerbert),
                        mongoTemplate.insert(scienceFiction))
                .flatMap(tuple -> mongoTemplate.insert(new Book(title, tuple.getT1(), tuple.getT2())))
                .flatMap(book ->
                        mongoTemplate.insertAll(
                                        List.of(new Comment(commentText1, book),
                                                new Comment(commentText2, book)))
                                .ignoreElements()
                                .thenReturn(book.getId())
                );

        var test = Mono.from(setup)
                .flatMap(bookId -> commentRepository.findCommentsByBookId(bookId).collectList());

        StepVerifier
                .create(test)
                .assertNext(result -> assertThat(result)
                        .hasSize(2)
                        .extracting(
                                Comment::getText,
                                it -> it.getBook().getTitle())
                        .containsAll(expected)
                )
                .expectComplete()
                .verify();
    }
}