package com.github.kondury.library.service;

import com.github.kondury.library.service.dto.GenreDto;
import com.github.kondury.library.service.mapper.GenreMapper;
import com.github.kondury.library.repository.GenreRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    @CircuitBreaker(name = "default-service")
    @Transactional(readOnly = true)
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(genreMapper::genreToGenreDto)
                .toList();
    }

    @Override
    @CircuitBreaker(name = "default-service")
    @Transactional(readOnly = true)
    public Optional<GenreDto> findById(long id) {
        return genreRepository.findById(id)
                .map(genreMapper::genreToGenreDto);
    }
}
