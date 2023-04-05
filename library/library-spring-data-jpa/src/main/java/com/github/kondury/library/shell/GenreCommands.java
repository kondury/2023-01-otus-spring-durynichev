package com.github.kondury.library.shell;

import com.github.kondury.library.service.GenreService;
import com.github.kondury.library.service.coverter.GenreConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class GenreCommands {

    private final GenreService genreService;
    private final GenreConverter genreConverter;

    @ShellMethod(value = "returns all genres accessible in database", key = {"find-all-genres", "find-genres"})
    String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::convert)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "returns genre by id", key = {"find-genre-by-id", "find-genre"})
    String findGenreById(long id) {
        return genreService.findById(id)
                .map(genreConverter::convert)
                .orElse("Genre is not found: id=" + id);
    }
}
