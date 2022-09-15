package org.example.dao;

import org.example.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    Optional<Genre> findByName(String name);

    List<Genre> findAll();

    Genre insert(Genre genre);

    void deleteByName(String name);

}
