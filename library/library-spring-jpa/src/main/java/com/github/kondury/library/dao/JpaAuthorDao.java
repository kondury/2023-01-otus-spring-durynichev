package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaAuthorDao implements AuthorDao {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Author> findAll() {
        var query = em.createQuery("select author from Author author", Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public Author getReferenceById(Long id) {
        return em.getReference(Author.class, id);
    }
}
