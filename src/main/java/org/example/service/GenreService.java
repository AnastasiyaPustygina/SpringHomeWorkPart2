package org.example.service;

import org.example.domain.Genre;

import java.util.List;

public interface GenreService {

    Genre findByName(String name);

    List<Genre> findAll();

    Genre insert(Genre genre);

    void deleteByName(String name);

}
