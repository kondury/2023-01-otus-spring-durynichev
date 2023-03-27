package com.github.kondury.library.shell;

import com.github.kondury.library.domain.Book;
import com.github.kondury.library.service.AuthorService;
import com.github.kondury.library.service.BookService;
import com.github.kondury.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class BookCommands {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @ShellMethod(value = "inserts book into database", key = {"add-book", "insert-book"})
    String insertBook(String title, long authorId, long genreId) {
        var genre = genreService.findById(genreId);
        var author = authorService.findById(authorId);
        var book = new Book(title, author.orElse(null), genre.orElse(null));
        var result = bookService.insert(book);
        return String.valueOf(result);
    }

    @ShellMethod(value = "returns all books accessible in database", key = {"find-all-books", "find-books"})
    String findAllBooks() {
        return bookService.findAll().stream().map(String::valueOf).collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "returns book by id", key = {"find-book-by-id", "find-book"})
    String findBookById(long id) {
        return bookService.findById(id).toString();
    }

    @ShellMethod(value = "updates book properties by id", key = "update-book")
    String updateBook(long id, String title, long authorId, long genreId) {
        var genre = genreService.findById(genreId);
        var author = authorService.findById(authorId);
        var result = bookService.update(new Book(id, title, author.orElse(null), genre.orElse(null)));
        return String.valueOf(result);
    }

    @ShellMethod(value = "deletes book by id", key = {"delete-book-by-id", "delete-book"})
    void deleteBookById(long id) {
        bookService.deleteById(id);
    }

}
