package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Comment;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({JpaCommentDao.class, JpaBookDao.class})
class JpaCommentDaoTest {

    @Autowired
    private BookDao bookDao;
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private TestEntityManager em;

    @Test
    void findByBookId_shouldReturnAllBookCommentsWithCorrectFields() {
        var dunaBookId = 1L;
        var expected = List.of(
                tuple("It's the real masterpiece!!! Must read!!!", dunaBookId, "Dune"),
                tuple("As for me it was a little bit too lingering and somewhat boring", dunaBookId, "Dune")
        );

        assertThat(commentDao.findByBookId(dunaBookId))
                .hasSize(2)
                .extracting(
                        Comment::getText,
                        it -> it.getBook().getId(),
                        it -> it.getBook().getTitle())
                .containsAll(expected);
    }

    @Test
    void findByIdCompleted_shouldReturnCommentWithExpectedId() {
        var expected = 1L;
        assertThat(commentDao.findByIdCompleted(expected))
                .isPresent()
                .map(Comment::getId)
                .containsSame(expected);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 4L, 1000L})
    void findByIdCompleted_shouldReturnEmptyOptionalIfCommentIsNotFoundById(long nonExistentId) {
        assertThat(commentDao.findByIdCompleted(nonExistentId)).isNotPresent();
    }

    @Test
    void save_shouldInsertCommentHavingIdAssignedAndExpectedFields() {
        String text = "A new comment";
        long bookId = 1L;
        Comment comment = new Comment(null, text,
                em.getEntityManager().getReference(Book.class, bookId));

        commentDao.save(comment);
        assertThat(comment.getId()).isNotNull();
        em.clear();

        assertThat(em.find(Comment.class, comment.getId()))
                .isNotNull()
                .doesNotReturn(null, from(Comment::getBook))
                .returns(bookId, from(it -> it.getBook().getId()))
                .returns(text, from(Comment::getText));
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, 3L, 1000L})
    void save_shouldThrowExceptionWhenInsertCommentWithBookHavingInvalidId(long bookId) {
        String text = "A new comment";
        Comment comment = new Comment(null, text, em.getEntityManager().getReference(Book.class, bookId));
        assertThatThrownBy(() -> commentDao.save(comment)).isInstanceOf(PersistenceException.class);
    }

    @Test
    void save_shouldUpdateCommentWithCorrectIdAndFields() {
        long existentCommentId = 1L;
        var commentToUpdate = em.find(Comment.class, existentCommentId);

        String expectedText = "A new comment";
        var expectedBookId = commentToUpdate.getBook().getId() == 1L ? 2L : 1L;
        var expectedBook = em.getEntityManager().getReference(Book.class, expectedBookId);
        var expectedComment = new Comment(existentCommentId, expectedText, expectedBook);
        em.clear();

        commentDao.save(expectedComment);
        em.flush();
        em.clear();

        assertThat(em.find(Comment.class, existentCommentId))
                .isNotNull()
                .doesNotReturn(null, from(Comment::getBook))
                .returns(existentCommentId, from(Comment::getId))
                .returns(expectedBook.getId(), from(it -> it.getBook().getId()))
                .returns(expectedText, from(Comment::getText));
    }

    @Test
    void delete_shouldDeleteBookById() {
        var existentBookId = 1L;
        var bookToRemove = em.find(Book.class, existentBookId);
        assertThat(bookToRemove).isNotNull();
        em.detach(bookToRemove);
        assertThat(bookDao.deleteById(existentBookId)).isTrue();
        assertThat(em.find(Book.class, existentBookId)).isNull();
        assertThat(bookDao.deleteById(existentBookId)).isFalse();
    }

    @Test
    void delete_shouldReturnFalseIfBookIdDoesNotExist() {
        var nonExistentBookId = 4L;
        assertThat(bookDao.deleteById(nonExistentBookId)).isFalse();
    }

}