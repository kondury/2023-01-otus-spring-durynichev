package com.github.kondury.library.service;

import com.github.kondury.library.domain.Book;
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
    public Book insert(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }

    @Override
    public Book update(String bookId, String title, String authorId, String genreId) {
        if (!bookRepository.existsById(bookId))
            throw new EntityDoesNotExist("The book does not exist: bookId=" + bookId);
        return save(bookId, title, authorId, genreId);
    }

    private Book save(String bookId, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityDoesNotExist("The author does not exist: authorId=" + authorId));
        var genre = Optional.ofNullable(genreId)
                .map(id -> genreRepository.findById(id)
                        .orElseThrow(() -> new EntityDoesNotExist("The genre does not exist: genreId=" + genreId)))
                .orElse(null);
        return bookRepository.save(new Book(bookId, title, author, genre));
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
