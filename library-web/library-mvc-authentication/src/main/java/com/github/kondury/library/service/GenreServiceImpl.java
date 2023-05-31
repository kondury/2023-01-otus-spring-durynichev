package com.github.kondury.library.service;

import com.github.kondury.library.service.dto.GenreDto;
import com.github.kondury.library.service.mapper.GenreMapper;
import com.github.kondury.library.repository.GenreRepository;
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

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAll() {
        System.out.println("Genre.findAll");
        return genreRepository.findAll().stream()
                .map(genreMapper::genreToGenreDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<GenreDto> findById(long id) {
        System.out.println("Genre.findById");
        return genreRepository.findById(id)
                .map(genreMapper::genreToGenreDto);
    }
}
