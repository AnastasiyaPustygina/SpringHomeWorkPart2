package org.example.service;

import org.example.domain.Author;

import java.util.List;

public interface AuthorService {

    Author findByName(String name);

    List<Author> findAll();

    Author insert(Author author);

    void deleteByName(String name);

}
