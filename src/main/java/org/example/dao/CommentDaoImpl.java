package org.example.dao;

import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Comment;
import org.example.domain.Genre;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentDaoImpl implements CommentDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.of(entityManager.find(Comment.class, id));
    }

    @Override
    public List<Comment> findAll() {
        TypedQuery<Comment> query = entityManager.createQuery("select c from Comment c", Comment.class);
        return query.getResultList();
    }

    @Override
    public Comment insert(Comment comment) {
        Book book = comment.getBook();
        if(book.getId() > 0) book = entityManager.merge(book);
        Author author = book.getAuthor().getId() > 0 ? entityManager.merge(book.getAuthor()) :
                book.getAuthor();
        Genre genre = book.getGenre().getId() > 0 ? entityManager.merge(book.getGenre()) :
                book.getGenre();
        Book resultingBook = Book.builder().id(book.getId()).title(book.getTitle())
                .text(book.getText()).author(author).genre(genre).comments(Collections.emptyList()).build();
        Comment resultingComment = Comment.builder().text(comment.getText()).book(resultingBook).build();
        entityManager.persist(resultingComment);
        return resultingComment;
    }

    @Override
    public void updateTextById(long id, String text) {
        Query query = entityManager.createQuery("update Comment c set c.text = :text" +
                " where c.id = :id");
        query.setParameter("text", text);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        Query query = entityManager.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
