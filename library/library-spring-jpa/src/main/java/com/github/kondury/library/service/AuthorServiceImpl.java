package com.github.kondury.library.service;

import com.github.kondury.library.dao.AuthorDao;
import com.github.kondury.library.domain.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return authorDao.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Author> findById(long id) {
        return authorDao.findById(id);
    }
}
