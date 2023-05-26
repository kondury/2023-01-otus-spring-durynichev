package com.github.kondury.library.rest;


import com.github.kondury.library.domain.Book;
import com.github.kondury.library.rest.dto.BookDto;
import com.github.kondury.library.rest.dto.CreateBookRequest;
import com.github.kondury.library.rest.dto.UpdateBookRequest;
import com.github.kondury.library.rest.mapper.BookMapper;
import com.github.kondury.library.repository.AuthorRepository;
import com.github.kondury.library.repository.BookRepository;
import com.github.kondury.library.repository.CommentRepository;
import com.github.kondury.library.repository.GenreRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;

    private final BookMapper bookMapper;

    @GetMapping("/api/books")
    public Flux<BookDto> findAll() {
        return bookRepository.findAll()
                .map(bookMapper::bookToBookDto);
    }

    @GetMapping("/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> findById(@PathVariable String id) {
        return bookRepository.findById(id)
                .map(bookMapper::bookToBookDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/api/books/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        return bookRepository.deleteById(id)
                .and(commentRepository.deleteByBookId(id));
    }

    @PostMapping("/api/books")
    public Mono<BookDto> addBook(@Valid @RequestBody CreateBookRequest request) {
        return save(null, request.title(), request.authorId(), request.genreId());
    }

    @PutMapping("/api/books/{id}")
    public Mono<BookDto> updateBook(@PathVariable String id,
                                    @Valid @RequestBody UpdateBookRequest request) {
        return bookRepository.existsById(request.id())
                .flatMap(exists -> exists
                        ? save(request.id(), request.title(), request.authorId(), request.genreId())
                        : Mono.error(() -> {
                            throw new EntityDoesNotExistException("The book does not exist: bookId=" + request.id());
                        }));
    }

    private Mono<BookDto> save(String bookId, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .switchIfEmpty(Mono.error(() -> {
                    throw new EntityDoesNotExistException("The author does not exist: authorId=" + authorId);
                }));
        var genre = genreRepository.findById(genreId)
                .switchIfEmpty(Mono.error(() -> {
                    throw new EntityDoesNotExistException("The genre does not exist: genreId=" + genreId);
                }));
        return Mono.zip(author, genre)
                .map(tuple -> new Book(bookId, title, tuple.getT1(), tuple.getT2()))
                .flatMap(bookRepository::save)
                .map(bookMapper::bookToBookDto);
    }
}
