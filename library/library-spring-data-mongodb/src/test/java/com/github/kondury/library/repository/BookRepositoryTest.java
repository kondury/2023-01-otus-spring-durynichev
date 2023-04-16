package com.github.kondury.library.repository;

import com.github.kondury.library.domain.Book;
import com.github.kondury.library.event.MongoBookCascadeDeleteEventsListener;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataMongoTest
@Import(MongoBookCascadeDeleteEventsListener.class)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void findAll_shouldReturnAllBooksWithCorrectAuthorsAndGenres() {
        var expected = List.of(
                tuple("Dune", "Frank Herbert", "Science Fiction"),
                tuple("Murder on the Orient Express", "Agatha Christie", "Detective"),
                tuple("Solaris", "Stanislaw Lem", "Science Fiction")
        );

        assertThat(bookRepository.findAll())
                .hasSize(3)
                .extracting(
                        Book::getTitle,
                        it -> it.getAuthor().getName(),
                        it -> it.getGenre().getName())
                .containsAll(expected);
    }

    @Test
    void delete_shouldDeleteBookByIdAndAllBookComments() {

        val books = bookRepository.findAll();
        val bookToDelete = books.get(0);

        bookRepository.deleteById(bookToDelete.getId());

        assertThat(commentRepository.findByBookId(bookToDelete.getId())).isEmpty();
        assertThat(commentRepository.findAll())
                .extracting(v -> v.getBook().getId())
                .doesNotContain(bookToDelete.getId());
    }
}