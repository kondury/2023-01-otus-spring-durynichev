package com.github.kondury.library.dao;


import com.github.kondury.library.domain.Book;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookDao implements BookDao {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public List<Book> findAllCompleted() {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        return em.createQuery("select book from Book book", Book.class)
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    public Optional<Book> findByIdCompleted(Long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        Map<String, Object> properties = Map.of("jakarta.persistence.fetchgraph", entityGraph);
        return Optional.ofNullable(em.find(Book.class, id, properties));
    }

    @Override
    public boolean deleteById(Long id) {
        var book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
            return true;
        }
        return false;
    }
}
