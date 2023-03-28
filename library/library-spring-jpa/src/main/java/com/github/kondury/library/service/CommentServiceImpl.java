package com.github.kondury.library.service;

import com.github.kondury.library.dao.BookDao;
import com.github.kondury.library.dao.CommentDao;
import com.github.kondury.library.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final BookDao bookDao;

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
        return commentDao.findByBookId(bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> findById(long id) {
        return commentDao.findByIdCompleted(id);
    }

    @Transactional
    @Override
    public boolean deleteById(long id) {
        return (commentDao.deleteById(id));
    }

    private Optional<Comment> save(Long commentId, long bookId, String text) {
        var book = bookDao.findByIdCompleted(bookId);
        if (book.isPresent()) {
            var comment = commentDao.save(new Comment(commentId, text, book.get()));
            return Optional.of(comment);
        }
        return Optional.empty();
    }
}
