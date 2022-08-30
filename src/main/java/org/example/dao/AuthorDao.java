package org.example.dao;


import org.example.domain.Author;

import java.util.List;

public interface AuthorDao {

    Author findById(long id);
    List<Author> findAll();
    long insert(Author author);
    void deleteById(long id);

}
