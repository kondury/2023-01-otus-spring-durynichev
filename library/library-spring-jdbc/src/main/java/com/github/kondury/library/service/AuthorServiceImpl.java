package com.github.kondury.library.service;

import com.github.kondury.library.dao.AuthorDao;
import com.github.kondury.library.domain.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Override
    public List<Author> findAll() {
        return authorDao.findAll();
    }

    @Override
    public Optional<Author> findById(long id) {
        return authorDao.findById(id);
    }
}
