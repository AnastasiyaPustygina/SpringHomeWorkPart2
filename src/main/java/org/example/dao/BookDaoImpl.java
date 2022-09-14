package org.example.dao;

import org.example.domain.Book;
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
        if(book.getId() == 0){
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void deleteByTitle(String title) {
        Query query = entityManager.createQuery("delete from Book b where b.title = :title");
        query.setParameter("title", title);
        query.executeUpdate();
    }
}
