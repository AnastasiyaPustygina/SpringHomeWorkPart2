package org.example.dao;

import java.util.*;
import javax.persistence.*;
import org.example.domain.Author;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorDaoImpl implements AuthorDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Author> findByName(String name){
        TypedQuery<Author> query = entityManager.createQuery("select a from Author a where a.name = :name",
                Author.class);
        query.setParameter("name", name);
        try{
            return Optional.ofNullable(query.getSingleResult());
        }catch (NoResultException e){
            return Optional.empty();
        }

    }

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = entityManager.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Author insert(Author author) {
        entityManager.persist(author);
        return author;
    }

    @Override
    public void deleteByName(String name){
        Query query = entityManager.createQuery("delete from Author a where a.name = :name");
        query.setParameter("name", name);
        query.executeUpdate();
    }

}
