package com.github.kondury.library.service;

import com.github.kondury.library.dao.AuthorRepository;
import com.github.kondury.library.dao.BookRepository;
import com.github.kondury.library.dao.GenreRepository;
import com.github.kondury.library.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public Optional<Book> insert(String title, long authorId, long genreId) {
        return save(null, title, authorId, genreId);
    }

    @Override
    @Transactional
    public Optional<Book> update(long id, String title, long authorId, long genreId) {
        return (bookRepository.existsById(id))
                ? save(id, title, authorId, genreId)
                : Optional.empty();
    }

    private Optional<Book> save(Long bookId, String title, long authorId, long genreId) {
        var genre = genreRepository.findById(genreId);
        var author = authorRepository.findById(authorId);

        if (genre.isPresent() && author.isPresent()) {
            var book = bookRepository.save(new Book(bookId, title, author.get(), genre.get()));
            return Optional.of(book);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

}
