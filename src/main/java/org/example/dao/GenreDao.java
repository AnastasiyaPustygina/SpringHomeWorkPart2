package org.example.dao;

import org.example.domain.Genre;

import java.util.List;

public interface GenreDao {

    Genre findByName(String name);

    List<Genre> findAll();

    Genre insert(Genre genre);

    void deleteByName(String name);

    void setGenres(List<Genre> genres);

}
