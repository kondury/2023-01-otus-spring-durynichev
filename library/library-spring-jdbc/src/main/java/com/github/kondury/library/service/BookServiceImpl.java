package com.github.kondury.library.service;

import com.github.kondury.library.dao.AuthorDao;
import com.github.kondury.library.dao.BookDao;
import com.github.kondury.library.dao.GenreDao;
import com.github.kondury.library.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    public Book insert(String title, long authorId, long genreId) {
        var genre = genreDao.findById(genreId);
        var author = authorDao.findById(authorId);
        var book = new Book(title, author.orElse(null), genre.orElse(null));
        return bookDao.insert(book);
    }

    @Override
    public Book update(long id, String title, long authorId, long genreId) {
        var genre = genreDao.findById(genreId);
        var author = authorDao.findById(authorId);
        return bookDao.update(new Book(id, title, author.orElse(null), genre.orElse(null)));
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public Optional<Book> findById(long id) {
        return bookDao.findById(id);
    }

    @Override
    public void deleteById(long id) {
        bookDao.deleteById(id);
    }
}
