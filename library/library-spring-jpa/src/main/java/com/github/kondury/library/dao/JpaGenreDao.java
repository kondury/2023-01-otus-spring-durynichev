package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaGenreDao implements GenreDao {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Genre> findAll() {
        var query = em.createQuery("select genre from Genre genre", Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public Genre getReferenceById(Long id) {
        return em.getReference(Genre.class, id);
    }
}
