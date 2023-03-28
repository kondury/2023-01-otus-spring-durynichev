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
    private CommentDao commentDao;
    @Autowired
    private TestEntityManager em;

    @Test
    void findByBookId_shouldReturnAllBookCommentsWithCorrectFields() {
        var duneBookId = 1L;
        var expected = List.of(
                tuple("It's the real masterpiece!!! Must read!!!", duneBookId, "Dune"),
                tuple("As for me it was a little bit too lingering and somewhat boring", duneBookId, "Dune")
        );

        assertThat(commentDao.findByBookId(duneBookId))
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
    void delete_shouldDeleteCommentById() {
        var existentCommentId = 1L;
        var commentToRemove = em.find(Comment.class, existentCommentId);

        assertThat(commentToRemove).isNotNull();
        em.detach(commentToRemove);

        assertThat(commentDao.deleteById(existentCommentId)).isTrue();
        assertThat(em.find(Comment.class, existentCommentId)).isNull();
        assertThat(commentDao.deleteById(existentCommentId)).isFalse();
    }

    @Test
    void delete_shouldReturnFalseIfCommentIdDoesNotExist() {
        var nonExistentCommentId = 5L;
        assertThat(commentDao.deleteById(nonExistentCommentId)).isFalse();
    }

}