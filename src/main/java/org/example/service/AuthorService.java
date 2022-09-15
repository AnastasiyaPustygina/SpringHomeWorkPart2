package org.example.service;

import java.util.List;
import org.example.domain.Author;

public interface AuthorService {

    Author findByName(String name);

    List<Author> findAll();

    Author insert(Author author);

    void deleteByName(String name);

}
