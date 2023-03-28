package com.github.kondury.library.service;

import com.github.kondury.library.dao.AuthorDao;
import com.github.kondury.library.dao.BookDao;
import com.github.kondury.library.dao.GenreDao;
import com.github.kondury.library.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Transactional
    @Override
    public Optional<Book> insert(String title, long authorId, long genreId) {
        return save(null, title, authorId, genreId);
    }

    @Transactional
    @Override
    public Optional<Book> update(long id, String title, long authorId, long genreId) {
        return save(id, title, authorId, genreId);
    }

    private Optional<Book> save(Long bookId, String title, long authorId, long genreId) {
        var genre = genreDao.findById(genreId);
        var author = authorDao.findById(authorId);
        if (genre.isPresent() && author.isPresent()) {
            var book = bookDao.save(new Book(bookId, title, author.get(), genre.get()));
            return Optional.of(book);
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookDao.findAllCompleted();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(long id) {
        return bookDao.findByIdCompleted(id);
    }

    @Transactional
    @Override
    public boolean deleteById(long id) {
        return bookDao.deleteById(id);
    }

}
