package com.github.kondury.library.shell;

import com.github.kondury.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class BookCommands {

    private final BookService bookService;

    @ShellMethod(value = "inserts book into database", key = {"add-book", "insert-book"})
    String insertBook(String title, long authorId, long genreId) {
        var result = bookService.insert(title, authorId, genreId);
        return String.valueOf(result);
    }

    @ShellMethod(value = "returns all books accessible in database", key = {"find-all-books", "find-books"})
    String findAllBooks() {
        return bookService.findAll().stream()
                .map(String::valueOf)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "returns book by id", key = {"find-book-by-id", "find-book"})
    String findBookById(long id) {
        return bookService.findById(id).toString();
    }

    @ShellMethod(value = "updates book properties by id", key = "update-book")
    String updateBook(long id, String title, long authorId, long genreId) {
        var result = bookService.update(id, title, authorId, genreId);
        return String.valueOf(result);
    }

    @ShellMethod(value = "deletes book by id", key = {"delete-book-by-id", "delete-book"})
    void deleteBookById(long id) {
        bookService.deleteById(id);
    }

}
