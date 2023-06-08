package com.github.kondury.library.repository;

import com.github.kondury.library.model.Author;
import com.github.kondury.library.model.Book;
import com.github.kondury.library.model.Comment;
import com.github.kondury.library.model.Genre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TestEntityManager em;

    @Test
    void findAllCompleted_shouldReturnAllBooksWithCorrectAuthorsAndGenres() {
        var expected = List.of(
                tuple("Dune", "Frank Herbert", "Science Fiction"),
                tuple("Murder on the Orient Express", "Agatha Christie", "Detective")
        );

        assertThat(bookRepository.findAll())
                .hasSize(2)
                .extracting(
                        Book::getTitle,
                        it -> it.getAuthor().getName(),
                        it -> it.getGenre().getName())
                .containsAll(expected);
    }

    @Test
    void findByIdCompleted_shouldReturnBookWithExpectedId() {
        var expected = 1L;
        assertThat(bookRepository.findById(expected))
                .isPresent()
                .map(Book::getId)
                .containsSame(expected);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 3L, 1000L})
    void findByIdCompleted_shouldReturnEmptyOptionalIfBookIsNotFoundById(long nonExistentId) {
        assertThat(bookRepository.findById(nonExistentId)).isNotPresent();
    }

    @Test
    void save_shouldInsertBookWithAssignedIdAndExpectedFields() {
        String title = "A new book";
        long authorId = 1L;
        long genreId = 2L;
        Book book = new Book(null, title,
                em.getEntityManager().getReference(Author.class, authorId),
                em.getEntityManager().getReference(Genre.class, genreId));

        bookRepository.save(book);
        assertThat(book.getId()).isNotNull();

        em.clear();

        assertThat(em.find(Book.class, book.getId()))
                .isNotNull()
                .doesNotReturn(null, from(Book::getAuthor))
                .doesNotReturn(null, from(Book::getGenre))
                .returns(authorId, from(it -> it.getAuthor().getId()))
                .returns(genreId, from(it -> it.getGenre().getId()))
                .returns(title, from(Book::getTitle));
    }

    @ParameterizedTest
    @CsvSource({"mocked book title, 1000, 1", "mocked book title, 1, 1000"})
    void save_shouldThrowExceptionWhenInsertBookWithAuthorOrGenreHavingInvalidIds(
            String title,
            long authorId,
            long genreId
    ) {
        Book book = new Book(null, title,
                em.getEntityManager().getReference(Author.class, authorId),
                em.getEntityManager().getReference(Genre.class, genreId));
        assertThatThrownBy(() -> bookRepository.save(book)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_shouldUpdateBookWithCorrectIdAndFields() {
        long existentBookId = 1L;
        var bookToUpdate = em.find(Book.class, existentBookId);
        var expectedAuthor = em.find(Author.class, bookToUpdate.getAuthor().getId() == 1L ? 2L : 1L);
        var expectedGenre = em.find(Genre.class, bookToUpdate.getGenre().getId() == 1L ? 2L : 1L);
        String expectedTitle = "New title";
        var expectedBook = new Book(existentBookId, expectedTitle, expectedAuthor, expectedGenre);
        em.clear();

        bookRepository.save(expectedBook);
        em.flush();
        em.clear();

        assertThat(em.find(Book.class, existentBookId))
                .isNotNull()
                .doesNotReturn(null, from(Book::getAuthor))
                .doesNotReturn(null, from(Book::getGenre))
                .returns(existentBookId, from(Book::getId))
                .returns(expectedAuthor.getId(), from(it -> it.getAuthor().getId()))
                .returns(expectedGenre.getId(), from(it -> it.getGenre().getId()))
                .returns(expectedTitle, from(Book::getTitle));
    }


    @Test
    void delete_shouldDeleteBookById() {
        var existentBookId = 1L;
        var bookToRemove = em.find(Comment.class, existentBookId);

        assertThat(bookToRemove).isNotNull();
        em.detach(bookToRemove);

        bookRepository.deleteById(existentBookId);

        em.flush();
        assertThat(em.find(Book.class, existentBookId)).isNull();
    }
}