package org.example.service;

import org.example.domain.Book;
import org.example.domain.Genre;

import java.util.List;

public interface GenreService {

    Genre findById(long id);
    List<Genre> findAll();
    long insert(Genre genre);
    void deleteById(long id);

}
