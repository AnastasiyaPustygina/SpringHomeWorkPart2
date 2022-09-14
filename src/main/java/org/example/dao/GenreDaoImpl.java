package org.example.dao;

import java.util.*;
import javax.persistence.*;
import org.example.domain.Genre;
import org.springframework.stereotype.Repository;

@Repository
public class GenreDaoImpl implements GenreDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Genre> findByName(String name) {
        TypedQuery<Genre> query = entityManager.createQuery(
                "select g from Genre g where g.name = :name", Genre.class);
        query.setParameter("name", name);
        try{
            return Optional.ofNullable(query.getSingleResult());
        }catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = entityManager.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Genre insert(Genre genre) {
        if(genre.getId() <= 0){
            entityManager.persist(genre);
            return genre;
        }
        return entityManager.merge(genre);
    }

    @Override
    public void deleteByName(String name) {
        Query query = entityManager.createQuery("delete from Genre g where g.name = :name");
        query.setParameter("name", name);
        query.executeUpdate();
    }
}
