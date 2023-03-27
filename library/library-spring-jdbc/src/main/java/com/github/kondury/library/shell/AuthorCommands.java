package com.github.kondury.library.shell;

import com.github.kondury.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCommands {

    private final AuthorService authorService;

    @ShellMethod(value = "returns all authors accessible in database", key = {"find-all-authors", "find-authors"})
    String findAllAuthors() {
        return authorService.findAll().stream().map(String::valueOf).collect(Collectors.joining("\n"));
    }
}
