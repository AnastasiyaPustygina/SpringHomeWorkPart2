package org.example.dao;

import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Genre;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.*;


@Repository
public class BookDaoImpl implements BookDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Book> findByTitle(String title) {
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b where b.title = :title", Book.class);
        query.setParameter("title", title);
        try{
            return Optional.ofNullable(query.getSingleResult());
        }catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }

    @Override
    public Book insert(Book book) {
        Author author = book.getAuthor().getId() > 0 ? entityManager.merge(book.getAuthor()) :
                book.getAuthor();
        Genre genre = book.getGenre().getId() > 0 ? entityManager.merge(book.getGenre()) :
                book.getGenre();
        Book resultingBook = Book.builder().title(book.getTitle()).text(book.getText())
                .author(author).genre(genre).comments(Collections.emptyList()).build();
        entityManager.persist(resultingBook);
        return resultingBook;
    }

    @Override
    public void deleteByTitle(String title) {
        Query query = entityManager.createQuery("delete from Book b where b.title = :title");
        query.setParameter("title", title);
        query.executeUpdate();
    }
}
