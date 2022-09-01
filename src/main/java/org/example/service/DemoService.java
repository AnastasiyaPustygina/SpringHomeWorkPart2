package org.example.service;

public interface DemoService {

    void demo();
    void insertAuthor();
    void insertBook();
    void insertGenre();
    void deleteByAuthorName();
    void deleteByGenreName();
    void deleteByBookTitle();
    void printAuthorsByName();
    void printBooksByTitle();
    void printGenresByName();
    void printAllBooks();
    void printAllAuthors();
    void printAllGenres();

}
