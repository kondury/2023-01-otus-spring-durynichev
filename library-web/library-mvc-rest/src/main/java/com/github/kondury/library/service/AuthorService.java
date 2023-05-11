package com.github.kondury.library.service;

import com.github.kondury.library.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<AuthorDto> findAll();
    Optional<AuthorDto> findById(long id);
}
