package com.github.kondury.library.service;

import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Comment;
import com.github.kondury.library.repository.BookRepository;
import com.github.kondury.library.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void update_shouldPassCommentToDaoAndReturnWhatDaoReturned() {
        String commentId = "commentId";
        String text = "Comment text";
        String bookId = "bookId";
        Comment comment = mock(Comment.class);
        Book book = mock(Book.class);

        given(bookRepository.findById(anyString())).willReturn(Optional.of(book));
        given(commentRepository.save(any())).willReturn(comment);

        assertThat(commentService.update(commentId, bookId, text)).isEqualTo(Optional.of(comment));

        ArgumentCaptor<String> bookIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(bookRepository, times(1)).findById(bookIdCaptor.capture());
        assertThat(bookIdCaptor.getAllValues())
                .hasSize(1)
                .containsExactly(bookId);

        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository, times(1)).save(commentCaptor.capture());
        assertThat(commentCaptor.getAllValues())
                .hasSize(1)
                .extracting(Comment::getId, Comment::getText, Comment::getBook)
                .containsExactly(tuple(commentId, text, book));
    }

    @Test
    void insert_shouldPassCommentToDaoAndReturnWhatDaoReturned() {
        String text = "Comment text";
        String bookId = "bookId";
        Comment comment = mock(Comment.class);
        Book book = mock(Book.class);

        given(bookRepository.findById(anyString())).willReturn(Optional.of(book));
        given(commentRepository.save(any())).willReturn(comment);

        assertThat(commentService.insert(bookId, text)).isEqualTo(Optional.of(comment));

        ArgumentCaptor<String> bookIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(bookRepository, times(1)).findById(bookIdCaptor.capture());
        assertThat(bookIdCaptor.getAllValues())
                .hasSize(1)
                .containsExactly(bookId);

        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository, times(1)).save(commentCaptor.capture());
        assertThat(commentCaptor.getAllValues())
                .hasSize(1)
                .extracting(Comment::getId, Comment::getText, Comment::getBook)
                .containsExactly(tuple(null, text, book));
    }

    @Test
    void deleteById_shouldPassCommentIdToDao() {
        String passedId = "passedId";

        assertDoesNotThrow(() -> commentService.deleteById(passedId));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(commentRepository, times(1)).deleteById(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1).containsExactly(passedId);
    }

}