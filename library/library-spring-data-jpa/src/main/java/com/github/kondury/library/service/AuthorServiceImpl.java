package com.github.kondury.library.service;

import com.github.kondury.library.dao.AuthorRepository;
import com.github.kondury.library.domain.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }
}
