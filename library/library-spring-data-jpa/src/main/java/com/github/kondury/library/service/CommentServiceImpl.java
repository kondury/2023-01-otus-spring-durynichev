package com.github.kondury.library.service;

import com.github.kondury.library.dao.BookRepository;
import com.github.kondury.library.dao.CommentRepository;
import com.github.kondury.library.domain.Comment;
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

    @Transactional
    @Override
    public Optional<Comment> insert(long bookId, String text) {
        return save(null, bookId, text);
    }

    @Transactional
    @Override
    public Optional<Comment> update(long commentId, long bookId, String text) {
        return save(commentId, bookId, text);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Optional<Comment> save(Long commentId, long bookId, String text) {
        var comment = (commentId == null)
                ? new Comment()
                : commentRepository.getReferenceById(commentId);
        var book = bookRepository.getReferenceById(bookId);
        comment.setBook(book);
        comment.setText(text);
        comment = commentRepository.save(comment);
        return Optional.of(comment);
    }
}
