package org.example.service;

import org.example.domain.Book;

import java.util.List;

public interface BookService {

    Book findByTitle(String title);

    List<Book> findAll();

    Book insert(Book book);

    void deleteByTitle(String title);

}
