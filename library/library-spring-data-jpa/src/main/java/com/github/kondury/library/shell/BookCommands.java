package com.github.kondury.library.shell;

import com.github.kondury.library.service.BookService;
import com.github.kondury.library.service.coverter.BookConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class BookCommands {

    private final BookService bookService;
    private final BookConverter bookConverter;

    @ShellMethod(value = "inserts book into database", key = {"add-book", "insert-book"})
    String insertBook(String title, long authorId, long genreId) {
        return bookService.insert(title, authorId, genreId)
                .map(bookConverter::convert)
                .orElse("Book is not inserted. Check that a database contains genre and author with their respected ids.");
    }

    @ShellMethod(value = "returns all books accessible in database", key = {"find-all-books", "find-books"})
    String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::convert)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "returns book by id", key = {"find-book-by-id", "find-book"})
    String findBookById(long id) {
        return bookService.findById(id).map(bookConverter::convert)
                .orElse("Book is not found: id=" + id);
    }

    @ShellMethod(value = "updates book properties by id", key = "update-book")
    String updateBook(long id, String title, long authorId, long genreId) {
        return bookService.update(id, title, authorId, genreId)
                .map(bookConverter::convert)
                .orElse("Book is not updated. Check that a database contains genre and author with their respected ids.");
    }

    @ShellMethod(value = "deletes book by id", key = {"delete-book-by-id", "delete-book"})
    String deleteBookById(long id) {
        bookService.deleteById(id);
        return "Deleted.";
    }

}
