package com.github.kondury.library.service;

import com.github.kondury.library.dao.GenreRepository;
import com.github.kondury.library.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Genre> findById(long id) {
        return genreRepository.findById(id);
    }
}
