package com.github.kondury.library.service;

import com.github.kondury.library.service.dto.BookDto;
import com.github.kondury.library.service.dto.CreateBookRequest;
import com.github.kondury.library.service.dto.UpdateBookRequest;
import com.github.kondury.library.service.mapper.BookMapper;
import com.github.kondury.library.repository.AuthorRepository;
import com.github.kondury.library.repository.BookRepository;
import com.github.kondury.library.repository.GenreRepository;
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

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookDto insert(CreateBookRequest request) {
        return save(null, request.title(), request.authorId(), request.genreId());
    }

    @Override
    @Transactional
    public BookDto update(UpdateBookRequest request) {
        if (!bookRepository.existsById(request.id()))
            throw new EntityDoesNotExistException("The book does not exist: bookId=" + request.id());
        return save(request.id(), request.title(), request.authorId(), request.genreId());
    }

    private BookDto save(Long bookId, String title, long authorId, long genreId) {
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityDoesNotExistException("The genre does not exist: genreId=" + genreId));
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityDoesNotExistException("The author does not exist: authorId=" + authorId));

        var book = bookRepository.save(new Book(bookId, title, author, genre));
        return bookMapper.bookToBookDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::bookToBookDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::bookToBookDto);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

}
