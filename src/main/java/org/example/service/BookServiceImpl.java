package org.example.service;


import org.example.exception.*;
import org.example.domain.Book;
import org.example.dao.BookDao;
import org.example.dao.GenreDao;
import org.example.domain.Genre;
import org.example.domain.Author;
import org.example.dao.AuthorDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final GenreDao genreDao;
    private final AuthorDao authorDao;

    @Override
    public Book findByTitle(String title) {
        return bookDao.findByTitle(title).orElseThrow(() -> new BookNotFoundException(
                "book with title " + title + " was not found"));
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    @Transactional
    public Book insert(Book book) {
        Author author = getOrInsertAuthor(book.getAuthor());
        Genre genre = getOrInsertGenre(book.getGenre());
        if(bookDao.findByTitle(book.getTitle()).isPresent()) {
            throw new BookAlreadyExistsException("book with title " + book.getTitle() + " already exists");
        }
        return bookDao.save(Book.builder().title(book.getTitle())
            .text(book.getText()).author(author).genre(genre).build());
    }

    @Override
    @Transactional
    public void deleteByTitle(String title) {
        if(bookDao.findByTitle(title).isEmpty()){
            throw new BookNotFoundException("book with title " + title + " was not found");
        }
        bookDao.deleteByTitle(title);
    }

    private Author getOrInsertAuthor(Author author) {
        Optional<Author> optAuthor = authorDao.findByName(author.getName());
        return optAuthor.isEmpty() ? authorDao.save(author) : optAuthor.get();

    }
    private Genre getOrInsertGenre(Genre genre){
        Optional<Genre> optGenre = genreDao.findByName(genre.getName());
        return optGenre.isEmpty() ? genreDao.save(genre) : optGenre.get();
    }
}
