package org.example.dao;

import org.example.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Optional<Book> findByTitle(String title);

    List<Book> findAll();

    Book insert(Book book);

    void deleteByTitle(String title);

}
