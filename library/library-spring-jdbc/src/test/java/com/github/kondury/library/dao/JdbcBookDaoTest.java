package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Author;
import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Genre;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import({JdbcBookDao.class, JdbcGenreDao.class, JdbcAuthorDao.class})
class JdbcBookDaoTest {

    @Autowired
    BookDao bookDao;
    @Autowired
    GenreDao genreDao;
    @Autowired
    AuthorDao authorDao;

    @Test
    void findAll_shouldReturnAllBooksWithCorrectAuthorsAndGenres() {
        var expected = List.of(
                tuple("Dune", "Frank Herbert", "Science Fiction"),
                tuple("Murder on the Orient Express", "Agatha Christie", "Detective")
        );

        assertThat(bookDao.findAll())
                .hasSize(2)
                .extracting(
                        Book::title,
                        it -> it.author().name(),
                        it -> it.genre().name())
                .containsAll(expected);
    }

    @Test
    void findById_shouldReturnBookWithExpectedId() {
        var expected = 1L;
        assertThat(bookDao.findById(expected))
                .isPresent()
                .map(Book::id)
                .containsSame(expected);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 3L, 1000L})
    void findById_shouldReturnEmptyOptionalIfBookIsNotFoundById(long nonExistentId) {
        assertThat(bookDao.findById(nonExistentId)).isNotPresent();
    }

    @Test
    void insert_shouldReturnTheSameBookWithAssignedIdAndExpectedTitleAndAuthorIdAndGenreId() {
        String title = "A new book";
        long authorId = 1L;
        long genreId = 2L;
        Book book = new Book(
                title,
                new Author(authorId, "mocked author name"),
                new Genre(genreId, "mocked genre name"));

        assertThat(bookDao.insert(book))
                .isNotNull()
                .doesNotReturn(null, from(Book::author))
                .doesNotReturn(null, from(Book::genre))
                .returns(authorId, from(it -> it.author().id()))
                .returns(genreId, from(it -> it.genre().id()))
                .returns(title, from(Book::title))
                .is(new Condition<>(b -> b.id() > 0,"id is more then zero"));
    }

    @ParameterizedTest
    @CsvSource({"mocked book title, 1000, 1", "mocked book title, 1, 1000"})
    void insert_shouldThrowExceptionWhenInsertBookWithAuthorOrGenreHavingInvalidIds(
            String title,
            long authorId,
            long genreId
    ) {
        var book = new Book(
                title,
                new Author(authorId, "mocked author name"),
                new Genre(genreId, "mocked genre name"));
        assertThatThrownBy(() -> bookDao.insert(book)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void insert_shouldThrowIllegalArgumentExceptionWhenBookIsNotZero() {
        long notZeroBookId = 1000L;
        long existentAuthorId = 1L;
        long existentGenreId = 1L;
        var book = new Book(
                notZeroBookId,
                "mocked title",
                new Author(existentAuthorId, "mocked author name"),
                new Genre(existentGenreId, "mocked genre name"));
        assertThatThrownBy(() -> bookDao.insert(book)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update_shouldUpdateBookWithCorrectIdAndFields() {
        long existentBookId = 1L;
        var bookToUpdate = bookDao.findById(existentBookId).orElseThrow();

        var expectedAuthor = authorDao.findById(bookToUpdate.author().id() == 1 ? 2 : 1).orElseThrow();
        var expectedGenre = genreDao.findById(bookToUpdate.genre().id() == 1 ? 2 : 1).orElseThrow();
        String expectedTitle = "New title";

        var expectedBook = new Book(existentBookId, expectedTitle, expectedAuthor, expectedGenre);

        assertThat(bookDao.update(expectedBook))
                .isNotNull()
                .doesNotReturn(null, from(Book::author))
                .doesNotReturn(null, from(Book::genre))
                .returns(existentBookId, from(Book::id))
                .returns(expectedAuthor.id(), from(it -> it.author().id()))
                .returns(expectedGenre.id(), from(it -> it.genre().id()))
                .returns(expectedTitle, from(Book::title));
    }

    @ParameterizedTest
    @CsvSource({"1, mocked book title, 1000, 1", "1, mocked book title, 1, 1000"})
    void update_shouldThrowExceptionWhenInsertBookWithAuthorOrGenreHavingInvalidIds(
            long bookId,
            String title,
            long authorId,
            long genreId
    ) {
        var book = new Book(
                bookId,
                title,
                new Author(authorId, "mocked author name"),
                new Genre(genreId, "mocked genre name")
        );
        assertThatThrownBy(() -> bookDao.update(book)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void update_shouldThrowNullPointerExceptionIfBookIdDoesNotExist() {
        long nonExistentBookId = 4L;
        long existentAuthorId = 1L;
        long existentGenreId = 1L;
        var book = new Book(
                nonExistentBookId,
                "mocked title",
                new Author(existentAuthorId, "mocked author name"),
                new Genre(existentGenreId, "mocked genre name"));
        assertThatThrownBy(() -> bookDao.update(book)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void delete_shouldDeleteBookById() {
        var existentBookId = 1L;
        var bookToRemove = bookDao.findById(existentBookId)
                .orElseThrow(() ->
                        new NoSuchElementException("The book is not found by id: id=%s".formatted(existentBookId))
                );
        var expectedSize = bookDao.findAll().size() - 1;

        assertThat(bookDao.deleteById(bookToRemove.id())).isTrue();
        assertThat(bookDao.findAll())
                .hasSize(expectedSize)
                .doesNotContain(bookToRemove);
        assertThat(bookDao.deleteById(existentBookId)).isFalse();
    }

    @Test
    void delete_shouldReturnFalseIfBookIdDoesNotExist() {
        var nonExistentBookId = 4L;
        assertThat(bookDao.deleteById(nonExistentBookId)).isFalse();
    }
}