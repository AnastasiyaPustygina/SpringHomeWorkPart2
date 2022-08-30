package org.example.service;

import org.example.domain.Author;

import java.util.List;

public interface AuthorService {

    Author findById(long id);
    List<Author> findAll();
    long insert(Author author);
    void deleteById(long id);

}
