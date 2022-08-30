package org.example.dao;

import org.example.domain.Book;

import java.util.List;

public interface BookDao {

    Book findById(long id);

    List<Book> findAll();

    long insert(Book book);

    void deleteById(long id);

    void deleteBooksByAuthorId(long author_id);

    void deleteBooksByGenreId(long genre_id);
}
