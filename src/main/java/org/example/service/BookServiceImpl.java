package org.example.service;

import liquibase.pro.packaged.B;
import lombok.RequiredArgsConstructor;
import org.example.dao.AuthorDao;
import org.example.dao.BookDao;
import org.example.dao.GenreDao;
import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Genre;
import org.example.exception.AuthorNotFoundException;
import org.example.exception.BookAlreadyExistsException;
import org.example.exception.BookNotFoundException;
import org.example.exception.GenreNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final GenreDao genreDao;
    private final AuthorDao authorDao;

    @Override
    public Book findByTitle(String title) {
        return bookDao.findByTitle(title);
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public Book insert(Book book) {
        insertAuthorIfItDoesntExists(book.getAuthor());
        insertGenreIfItDoesntExists(book.getGenre());
        try {
            bookDao.findByTitle(book.getTitle());
            throw new BookAlreadyExistsException("book with title " + book.getTitle() + " already exists");
        } catch (BookNotFoundException e) {
            return bookDao.insert(Book.builder().id(book.getId()).title(book.getTitle()).text(book.getText())
                    .author(authorDao.findByName(book.getAuthor().getName())).genre(
                            genreDao.findByName(book.getGenre().getName())).build());
        }
    }

    @Override
    public void deleteByTitle(String title) {
        try{
            bookDao.findByTitle(title);
            bookDao.deleteByTitle(title);
        }catch (BookNotFoundException e){
            throw new BookNotFoundException("Book with title " + title + " was not found");
        }
    }
    private void insertAuthorIfItDoesntExists(Author author) {
        try{
            authorDao.findByName(author.getName());
        }catch (AuthorNotFoundException e){
            authorDao.insert(author);
        }
    }
    private void insertGenreIfItDoesntExists(Genre genre){
        try{
            genreDao.findByName(genre.getName());
        }catch (GenreNotFoundException e){
            genreDao.insert(genre);
        }
    }
}
