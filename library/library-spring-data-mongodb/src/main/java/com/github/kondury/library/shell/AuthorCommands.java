package com.github.kondury.library.shell;

import com.github.kondury.library.service.AuthorService;
import com.github.kondury.library.service.coverter.AuthorConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCommands {

    private final AuthorService authorService;
    private final AuthorConverter authorConverter;

    @ShellMethod(value = "returns all authors accessible in database", key = {"find-all-authors", "find-authors"})
    String findAllAuthors() {
        return authorService.findAll().stream()
                .map(authorConverter::convert)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "returns an author by id", key = {"find-author-by-id", "find-author"})
    String findAuthorById(String id) {
        return authorService.findById(id)
                .map(authorConverter::convert)
                .orElse("The author is not found: id=" + id);
    }

}
