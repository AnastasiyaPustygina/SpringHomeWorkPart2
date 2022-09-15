package org.example.service;

import java.util.List;
import org.example.domain.Book;

public interface BookService {

    Book findByTitle(String title);

    List<Book> findAll();

    Book insert(Book book);

    void deleteByTitle(String title);

}
