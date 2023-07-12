package com.github.kondury.library.repository;

import com.github.kondury.library.config.TestContainersConfig;
import com.github.kondury.library.model.Book;
import com.github.kondury.library.model.Comment;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest(classes = TestContainersConfig.class)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EntityManager em;

    @Test
    void findByBookId_shouldReturnAllBookCommentsWithCorrectFields() {
        var duneBookId = 1L;
        var expected = List.of(
                tuple("It's the real masterpiece!!! Must read!!!", duneBookId, "Dune"),
                tuple("As for me it was a little bit too lingering and somewhat boring", duneBookId, "Dune")
        );

        assertThat(commentRepository.findByBookId(duneBookId))
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
        assertThat(commentRepository.findById(expected))
                .isPresent()
                .map(Comment::getId)
                .containsSame(expected);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 4L, 1000L})
    void findByIdCompleted_shouldReturnEmptyOptionalIfCommentIsNotFoundById(long nonExistentId) {
        assertThat(commentRepository.findById(nonExistentId)).isNotPresent();
    }

    @Test
    void save_shouldInsertCommentHavingIdAssignedAndExpectedFields() {
        String text = "A new comment";
        long bookId = 1L;
        Comment comment = new Comment(null, text,
                em.getReference(Book.class, bookId));

        commentRepository.save(comment);
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
        Comment comment = new Comment(null, text, em.getReference(Book.class, bookId));
        assertThatThrownBy(() -> commentRepository.save(comment)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_shouldUpdateCommentWithCorrectIdAndFields() {
        long existentCommentId = 1L;
        var commentToUpdate = em.find(Comment.class, existentCommentId);

        String expectedText = "A new comment";
        var expectedBookId = commentToUpdate.getBook().getId() == 1L ? 2L : 1L;
        var expectedBook = em.getReference(Book.class, expectedBookId);
        var expectedComment = new Comment(existentCommentId, expectedText, expectedBook);
        em.clear();

        commentRepository.save(expectedComment);
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

        commentRepository.deleteById(existentCommentId);

        em.flush();
        assertThat(em.find(Comment.class, existentCommentId)).isNull();
    }
}