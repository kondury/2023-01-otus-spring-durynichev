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

    public static final String UNKNOWN_GENRE = "<UNKNOWN GENRE>";

    private final BookService bookService;
    private final BookConverter bookConverter;

    @ShellMethod(value = "inserts a book into database, use '<UNKNOWN GENRE>' to insert book without any genre",
            key = {"add-book", "insert-book"})
    String insertBook(String title, String authorId, String genreId) {
        try {
            var book = bookService.insert(title, authorId, getGenreId(genreId));
            return bookConverter.convert(book);
        } catch (RuntimeException e) {
            System.out.println("The book is not inserted. For more information use the 'stacktrace' command");
            throw e;
        }
    }

    @ShellMethod(value = "returns all books accessible in the database", key = {"find-all-books", "find-books"})
    String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::convert)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "returns a book by id", key = {"find-book-by-id", "find-book"})
    String findBookById(String id) {
        return bookService.findById(id).map(bookConverter::convert)
                .orElse("The book is not found: id=" + id);
    }

    @ShellMethod(value = "updates a book properties by id, use '<UNKNOWN GENRE>' to remove book's genre",
            key = "update-book")
    String updateBook(String id, String title, String authorId, String genreId) {
        try {
            var book = bookService.update(id, title, authorId, getGenreId(genreId));
            return bookConverter.convert(book);
        } catch (RuntimeException e) {
            System.out.println("The book is not updated. For more information use the 'stacktrace' command");
            throw e;
        }
    }

    @ShellMethod(value = "deletes a book by id", key = {"delete-book-by-id", "delete-book"})
    String deleteBookById(String id) {
        bookService.deleteById(id);
        return "The delete command is executed.";
    }

    private String getGenreId(String genreIdParameter) {
        return UNKNOWN_GENRE.equals(genreIdParameter) ? null : genreIdParameter;
    }

}
