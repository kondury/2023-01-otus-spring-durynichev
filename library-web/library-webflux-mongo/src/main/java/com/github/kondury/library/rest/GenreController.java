package com.github.kondury.library.rest;


import com.github.kondury.library.rest.dto.GenreDto;
import com.github.kondury.library.rest.mapper.GenreMapper;
import com.github.kondury.library.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
public class GenreController {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @GetMapping("/api/genres")
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll()
                .map(genreMapper::genreToGenreDto);
    }
}
