package com.github.kondury.library.controller;

import com.github.kondury.library.dto.BookDto;
import com.github.kondury.library.dto.CreateBookRequest;
import com.github.kondury.library.dto.UpdateBookRequest;
import com.github.kondury.library.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@Controller
public class LibraryController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentService commentService;

    @GetMapping({"/", "/books/list"})
    public String listBooks(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "books/bookList";
    }

    @GetMapping("/books/new")
    public String newBook(Model model) {
        populateLists(model);
        return "books/bookNew";
    }

    @PostMapping("/books/new")
    public String addBook(@Valid @ModelAttribute("newBook") CreateBookRequest request,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            populateLists(model);
            return "books/bookNew";
        }
        bookService.insert(request);
        return "redirect:/";
    }

    @GetMapping("/books/{id}/edit")
    public String editBook(@PathVariable Long id, Model model) {
        var bookDto = bookService.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistException("The book does not exist: bookId=" + id));
        populateEditModel(bookDto, model);
        return "books/bookEdit";
    }

    @PostMapping("/books/{id}/edit")
    public String updateBook(@PathVariable Long id, @Valid @ModelAttribute("updateBook") UpdateBookRequest request,
                             BindingResult bindingResult, Model model) {
        assert Objects.equals(id, request.id());
        if (bindingResult.hasErrors()) {
            var authorDto = authorService.findById(request.authorId())
                    .orElseThrow(() -> new EntityDoesNotExistException("The author does not exist: authorId="
                            + request.authorId()));
            var genreDto = genreService.findById(request.genreId())
                    .orElseThrow(() -> new EntityDoesNotExistException("The genre does not exist: genreId="
                            + request.genreId()));
            var bookDto = new BookDto(request.id(), request.title(), authorDto, genreDto);
            populateEditModel(bookDto, model);
            return "books/bookEdit";
        }
        bookService.update(request);
        return "redirect:/";
    }

    @GetMapping("/books/{id}/comments/list")
    public String listBookComments(@PathVariable Long id, Model model) {
        var bookDto = bookService.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistException("The book does not exist: bookId=" + id));
        var comments = commentService.findByBookId(id);
        model.addAttribute("bookDto", bookDto);
        model.addAttribute("comments", comments);
        return "books/bookCommentsList";
    }


    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }

    @ExceptionHandler(EntityDoesNotExistException.class)
    public ResponseEntity<String> handleNotFound(EntityDoesNotExistException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict() {
    }

    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<String> databaseError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error: " + ex.getMessage());
    }

    private void populateEditModel(BookDto bookDto, Model model) {
        model.addAttribute("bookDto", bookDto);
        populateLists(model);
    }

    private void populateLists(Model model) {
        model.addAttribute("allAuthors", authorService.findAll());
        model.addAttribute("allGenres", genreService.findAll());
    }

}