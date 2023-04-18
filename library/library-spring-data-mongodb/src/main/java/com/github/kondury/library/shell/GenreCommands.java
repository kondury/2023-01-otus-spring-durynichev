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

    @ShellMethod(value = "returns all genres accessible in the database", key = {"find-all-genres", "find-genres"})
    String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::convert)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "returns a genre by id", key = {"find-genre-by-id", "find-genre"})
    String findGenreById(String id) {
        return genreService.findById(id)
                .map(genreConverter::convert)
                .orElse("A genre is not found: id=" + id);
    }

    @ShellMethod(value = "deletes a genre by id", key = {"delete-genre-by-id", "delete-genre"})
    String deleteGenreById(String id) {
        genreService.deleteById(id);
        return "The delete command is executed.";
    }
}
