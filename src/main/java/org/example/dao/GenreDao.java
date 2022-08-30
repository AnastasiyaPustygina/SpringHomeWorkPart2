package org.example.dao;

import org.example.domain.Genre;

import java.util.List;

public interface GenreDao {

    Genre findById(long id);
    List<Genre> findAll();
    long insert(Genre genre);
    void deleteById(long id);

}
