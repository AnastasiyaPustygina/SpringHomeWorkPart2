package org.example.dao;


import org.example.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

    Optional<Author> findByName(String name);

    List<Author> findAll();

    Author insert(Author author);

    void deleteByName(String name);

}
