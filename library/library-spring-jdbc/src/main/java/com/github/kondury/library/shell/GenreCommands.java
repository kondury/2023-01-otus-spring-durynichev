package com.github.kondury.library.shell;

import com.github.kondury.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class GenreCommands {

    private final GenreService genreService;

    @ShellMethod(value = "returns all genres accessible in database", key = {"find-all-genres", "find-genres"})
    String findAllGenres() {
        return genreService.findAll().stream()
                .map(String::valueOf)
                .collect(Collectors.joining("\n"));
    }
}
