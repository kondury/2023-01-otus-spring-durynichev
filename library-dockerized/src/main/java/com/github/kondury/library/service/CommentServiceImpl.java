package com.github.kondury.library.service;

import com.github.kondury.library.model.Comment;
import com.github.kondury.library.repository.BookRepository;
import com.github.kondury.library.repository.CommentRepository;
import com.github.kondury.library.service.dto.CommentDto;
import com.github.kondury.library.service.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto insert(long bookId, String text) {
        return save(null, bookId, text);
    }

    @Override
    @Transactional
    public CommentDto update(long commentId, long bookId, String text) {
        return save(commentId, bookId, text);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId).stream()
                .map(commentMapper::commentToCommentDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(long id) {
        return commentRepository.findById(id)
                .map(commentMapper::commentToCommentDto);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private CommentDto save(Long commentId, long bookId, String text) {
        var comment = Optional.ofNullable(commentId)
                .map(commentRepository::getReferenceById)
                .orElseGet(Comment::new);
        var book = bookRepository.getReferenceById(bookId);
        comment.setBook(book);
        comment.setText(text);
        comment = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }
}
