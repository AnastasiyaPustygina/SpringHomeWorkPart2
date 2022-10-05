package org.example.service;

import org.checkerframework.checker.nullness.Opt;
import org.example.dao.AuthorDao;
import org.example.dao.BookDao;
import org.example.dao.GenreDao;
import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Genre;
import org.example.exception.AuthorAlreadyExistsException;
import org.example.exception.BookAlreadyExistsException;
import org.example.exception.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс BookService")
@ActiveProfiles("test")
public class BookServiceTest {

    private static final int EXISTING_BOOK_ID = 1;
    private static final String EXISTING_BOOK_TITLE = "Lovers of death";
    private static final String EXISTING_BOOK_TEXT = "text of the book with name <<lovers of death>>";
    private static final int EXISTING_AUTHOR_ID = 1;
    private static final String EXISTING_AUTHOR_NAME = "Boris Akunin";
    private static final int EXISTING_GENRE_ID = 1;
    private static final String EXISTING_GENRE_NAME = "detective";
    private static final int NEW_BOOK_ID = 4;
    private static final String NEW_BOOK_TITLE = "The player who climbed to the top";
    private static final String NEW_BOOK_TEXT = "text of the book with name" +
            " <<The player who climbed to the top>>";
    private static final String NEW_AUTHOR_NAME = "Leach23";
    private static final String NEW_GENRE_NAME = "fantastic";

    @Mock
    private BookDao dao;
    @Mock
    private GenreDao genreDao;
    @Mock
    private AuthorDao authorDao;

    private BookService bookService;

    private final List<Book> books = new ArrayList<>();

    @BeforeEach
    void setUp(){
        bookService = new BookServiceImpl(dao, genreDao, authorDao);
        books.clear();
        Book book1 = Book.builder().id(EXISTING_BOOK_ID).title(EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT)
                .author(Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build())
                .genre(Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build()).build();
        books.add(book1);
    }

    @DisplayName("должен найти книгу по названию")
    @Test
    void shouldFindBookByTitle(){
        given(dao.findByTitle(EXISTING_BOOK_TITLE)).willReturn(Optional.of(books.get(0)));
        assertEquals(books.get(0), bookService.findByTitle(EXISTING_BOOK_TITLE));
    }

    @DisplayName("должен найти все книги")
    @Test
    void shouldFindAllBooks(){
        given(dao.findAll()).willReturn(books);
        assertEquals(books, bookService.findAll());
    }

    @DisplayName("должен удалить книгу по имени")
    @Test
    void shouldDeleteBookByName(){
        given(dao.findAll()).willReturn(books);
        long sizeBefore = dao.findAll().size();
        given(dao.findByTitle(EXISTING_BOOK_TITLE)).willReturn(Optional.of(books.get(0)));
        bookService.deleteByTitle(EXISTING_BOOK_TITLE);
        assertAll(
                () -> assertEquals(sizeBefore - 1, authorDao.findAll().size()),
                () -> assertThrows(BookNotFoundException.class, () -> bookService.deleteByTitle(NEW_BOOK_TITLE))
        );
    }

    @DisplayName("должен добавить книгу")
    @Test
    void shouldInsertBook(){
        Book book = Book.builder().id(NEW_BOOK_ID).title(NEW_BOOK_TITLE).text(NEW_BOOK_TEXT)
                .author(Author.builder().name(NEW_AUTHOR_NAME).build())
                .genre(Genre.builder().name(NEW_GENRE_NAME).build()).build();
        given(dao.save(any())).willReturn(book);
        given(dao.findByTitle(EXISTING_BOOK_TITLE)).willReturn(Optional.of(books.get(0)));
        given(dao.findByTitle(NEW_BOOK_TITLE)).willReturn(Optional.empty());
        given(authorDao.findByName(NEW_AUTHOR_NAME)).willReturn(Optional.empty());
        given(genreDao.findByName(NEW_GENRE_NAME)).willReturn(Optional.empty());

        assertAll(
                () -> assertEquals(bookService.insert(Book.builder().title(NEW_BOOK_TITLE).text(NEW_BOOK_TEXT)
                        .author(Author.builder().name(NEW_AUTHOR_NAME).build())
                        .genre(Genre.builder().name(NEW_GENRE_NAME).build()).build()).getTitle(), book.getTitle()),
                () -> assertThrows(BookAlreadyExistsException.class, () -> bookService.insert(
                        books.get(0)))
        );
    }
}
