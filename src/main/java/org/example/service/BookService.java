package org.example.service;

import org.example.domain.Book;

import java.util.List;

public interface BookService {

    Book findById(long id);
    List<Book> findAll();
    long insert(Book book);
    void deleteById(long id);

}
