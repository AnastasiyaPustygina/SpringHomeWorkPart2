package org.example.dao;

import org.example.domain.Book;

import java.util.List;

public interface BookDao {

    Book findByTitle(String title);

    List<Book> findAll();

    Book insert(Book book);

    void deleteByTitle(String title);

    void deleteBooksByAuthorId(long author_id);

    void deleteBooksByGenreId(long genre_id);

    void setBooks(List<Book> books);
}
