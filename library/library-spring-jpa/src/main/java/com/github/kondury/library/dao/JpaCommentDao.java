package com.github.kondury.library.dao;

import com.github.kondury.library.domain.Comment;
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
public class JpaCommentDao implements CommentDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<Comment> findByBookId(Long bookId) {
        return em.createQuery(
                        """
                                select comment from Comment comment
                                join fetch comment.book
                                where comment.book.id = :id""",
                        Comment.class
                ).setParameter("id", bookId)
                .getResultList();
    }

    @Override
    public Optional<Comment> findByIdCompleted(Long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("comment-book-entity-graph");
        Map<String, Object> properties = Map.of("jakarta.persistence.fetchgraph", entityGraph);
        return Optional.ofNullable(em.find(Comment.class, id, properties));
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            em.persist(comment);
            return comment;
        }
        return em.merge(comment);
    }

    @Override
    public boolean deleteById(Long id) {
        var comment = em.find(Comment.class, id);
        if (comment != null) {
            em.remove(comment);
            return true;
        }
        return false;
    }
}
