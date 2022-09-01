package org.example.dao;


import org.example.domain.Author;

import java.util.List;

public interface AuthorDao {

    Author findByName(String name);

    List<Author> findAll();

    Author insert(Author author);

    void deleteByName(String name);

    void setAuthors(List<Author> authors);

}
