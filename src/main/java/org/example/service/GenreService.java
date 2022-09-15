package org.example.service;

import java.util.List;
import org.example.domain.Genre;


public interface GenreService {

    Genre findByName(String name);

    List<Genre> findAll();

    Genre insert(Genre genre);

    void deleteByName(String name);

}
