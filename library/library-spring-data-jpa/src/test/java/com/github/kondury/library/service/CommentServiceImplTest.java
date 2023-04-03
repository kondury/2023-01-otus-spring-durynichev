package com.github.kondury.library.service;

import com.github.kondury.library.dao.BookRepository;
import com.github.kondury.library.dao.CommentRepository;
import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        long commentId = 1L;
        String text = "Comment text";
        long bookId = 1L;
        Comment comment = mock(Comment.class);
        Book book = mock(Book.class);

        given(commentRepository.getReferenceById(anyLong())).willReturn(comment);
        given(bookRepository.getReferenceById(anyLong())).willReturn(book);
        given(commentRepository.save(any())).willReturn(comment);

        doNothing().when(comment).setText(anyString());
        doNothing().when(comment).setBook(any());

        assertThat(commentService.update(commentId, bookId, text)).isEqualTo(Optional.of(comment));

        ArgumentCaptor<Long> bookIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(bookRepository, times(1)).getReferenceById(bookIdCaptor.capture());
        assertThat(bookIdCaptor.getAllValues())
                .hasSize(1)
                .containsExactly(bookId);

        ArgumentCaptor<String> textCaptor = ArgumentCaptor.forClass(String.class);
        verify(comment, times(1)).setText(textCaptor.capture());
        assertThat(textCaptor.getAllValues())
                .hasSize(1)
                .containsExactly(text);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(comment, times(1)).setBook(bookCaptor.capture());
        assertThat(bookCaptor.getAllValues())
                .hasSize(1)
                .containsExactly(book);

        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository, times(1)).save(commentCaptor.capture());
        assertThat(commentCaptor.getAllValues())
                .hasSize(1)
                .containsExactly(comment);

    }

    @Test
    void insert_shouldPassCommentToDaoAndReturnWhatDaoReturned() {
        String text = "Comment text";
        long bookId = 1L;
        Book book = mock(Book.class);
        Comment comment = mock(Comment.class);

        given(bookRepository.getReferenceById(anyLong())).willReturn(book);
        given(commentRepository.save(any())).willReturn(comment);

        assertThat(commentService.insert(bookId, text)).isEqualTo(Optional.of(comment));

        ArgumentCaptor<Long> bookIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(bookRepository, times(1)).getReferenceById(bookIdCaptor.capture());
        assertThat(bookIdCaptor.getAllValues())
                .hasSize(1)
                .containsExactly(bookId);

        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository, times(1)).save(commentCaptor.capture());
        assertThat(commentCaptor.getAllValues())
                .hasSize(1)
                .extracting(Comment::getText, Comment::getBook)
                .containsExactly(tuple(text, book));
    }

}