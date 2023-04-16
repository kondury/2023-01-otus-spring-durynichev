package com.github.kondury.library.service;

import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Genre;
import com.github.kondury.library.repository.AuthorRepository;
import com.github.kondury.library.repository.BookRepository;
import com.github.kondury.library.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> insert(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }

    @Override
    public Optional<Book> update(String id, String title, String authorId, String genreId) {
        return (bookRepository.existsById(id))
                ? save(id, title, authorId, genreId)
                : Optional.empty();
    }

    private Optional<Book> save(String bookId, String title, String authorId, String genreId) {
        Optional<Genre> genre = genreId != null ? genreRepository.findById(genreId) : Optional.empty();
        var author = authorRepository.findById(authorId);

        if (author.isPresent()) {
            var book = bookRepository.save(new Book(bookId, title, author.get(), genre.orElse(null)));
            return Optional.of(book);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

}
