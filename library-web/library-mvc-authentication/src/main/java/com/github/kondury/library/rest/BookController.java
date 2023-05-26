package com.github.kondury.library.rest;


import com.github.kondury.library.dto.BookDto;
import com.github.kondury.library.dto.CreateBookRequest;
import com.github.kondury.library.dto.UpdateBookRequest;
import com.github.kondury.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class BookController {
    private final BookService bookService;

    @GetMapping("/api/books")
    public List<BookDto> findAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/books/{id}")
    public Optional<BookDto> findBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @DeleteMapping("/api/books/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PostMapping("/api/books")
    public BookDto addBook(@Valid @RequestBody CreateBookRequest request) {
        return bookService.insert(request);
    }

    @PutMapping("/api/books/{id}")
    public BookDto updateBook(@PathVariable Long id, @Valid @RequestBody UpdateBookRequest request) {
        assert Objects.equals(id, request.id());
        return bookService.update(request);
    }
}
