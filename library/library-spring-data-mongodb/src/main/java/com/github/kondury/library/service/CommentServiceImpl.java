package com.github.kondury.library.service;

import com.github.kondury.library.domain.Comment;
import com.github.kondury.library.repository.BookRepository;
import com.github.kondury.library.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Override
    public Optional<Comment> insert(String bookId, String text) {
        return save(null, bookId, text);
    }

    @Override
    public Optional<Comment> update(String commentId, String bookId, String text) {
        return save(commentId, bookId, text);
    }

    @Override
    public List<Comment> findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Optional<Comment> save(String commentId, String bookId, String text) {
        var book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            var comment = new Comment(commentId, text, book.get());
            return Optional.of(commentRepository.save(comment));
        }
        return Optional.empty();
    }

}
