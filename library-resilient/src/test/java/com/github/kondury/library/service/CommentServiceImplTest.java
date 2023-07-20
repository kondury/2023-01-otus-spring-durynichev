package com.github.kondury.library.service;

import com.github.kondury.library.model.Book;
import com.github.kondury.library.model.Comment;
import com.github.kondury.library.repository.BookRepository;
import com.github.kondury.library.repository.CommentRepository;
import com.github.kondury.library.service.dto.CommentDto;
import com.github.kondury.library.service.mapper.CommentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void update_shouldPassCommentToDaoAndReturnWhatDaoReturned() {
        long commentId = 1L;
        String text = "Comment text";
        long bookId = 1L;
        Comment comment = mock(Comment.class);
        Book book = mock(Book.class);
        CommentDto commentDtoMock = new CommentDto(null, null, null);

        given(commentRepository.getReferenceById(anyLong())).willReturn(comment);
        given(bookRepository.getReferenceById(anyLong())).willReturn(book);
        given(commentRepository.save(any())).willReturn(comment);

        doNothing().when(comment).setText(anyString());
        doNothing().when(comment).setBook(any());

        given(commentMapper.commentToCommentDto(any())).willReturn(commentDtoMock);

        assertThat(commentService.update(commentId, bookId, text)).isEqualTo(commentDtoMock);

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
        verify(commentMapper, times(1)).commentToCommentDto(commentCaptor.capture());
        assertThat(commentCaptor.getAllValues())
                .hasSize(2)
                .containsExactly(comment, comment);
    }

    @Test
    void insert_shouldPassCommentToDaoAndReturnWhatDaoReturned() {
        String text = "Comment text";
        long bookId = 1L;
        Book book = mock(Book.class);
        Comment comment = mock(Comment.class);
        CommentDto commentDtoMock = new CommentDto(null, null, null);

        given(bookRepository.getReferenceById(anyLong())).willReturn(book);
        given(commentRepository.save(any())).willReturn(comment);
        given(commentMapper.commentToCommentDto(any())).willReturn(commentDtoMock);


        assertThat(commentService.insert(bookId, text)).isEqualTo(commentDtoMock);

        ArgumentCaptor<Long> bookIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(bookRepository, times(1)).getReferenceById(bookIdCaptor.capture());
        assertThat(bookIdCaptor.getAllValues())
                .hasSize(1)
                .containsExactly(bookId);

        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository, times(1)).save(commentCaptor.capture());
        verify(commentMapper, times(1)).commentToCommentDto(commentCaptor.capture());

        assertThat(commentCaptor.getAllValues())
                .hasSize(2)
                .extracting(Comment::getText, Comment::getBook)
                .containsExactly(tuple(text, book), tuple(null, null));
    }

}