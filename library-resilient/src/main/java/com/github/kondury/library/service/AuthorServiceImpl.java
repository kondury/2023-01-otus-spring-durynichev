package com.github.kondury.library.service;

import com.github.kondury.library.repository.AuthorRepository;
import com.github.kondury.library.service.dto.AuthorDto;
import com.github.kondury.library.service.mapper.AuthorMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    @CircuitBreaker(name = "default-service")
    @Transactional(readOnly = true)
    public List<AuthorDto> findAll() {
        System.out.println("Author.findAll");
        return authorRepository.findAll().stream()
                .map(authorMapper::authorToAuthorDto)
                .toList();
    }

    @Override
    @CircuitBreaker(name = "default-service")
    @Transactional(readOnly = true)
    public Optional<AuthorDto> findById(long id) {
        System.out.println("Author.findById");
        return authorRepository.findById(id)
                .map(authorMapper::authorToAuthorDto);
    }
}
