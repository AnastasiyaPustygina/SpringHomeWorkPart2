package org.example.service;

import org.example.dao.AuthorDao;
import org.example.dao.BookDao;
import org.example.dao.GenreDao;
import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Genre;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@DisplayName("Класс BookService")
@ActiveProfiles("test")
public class BookServiceTest {

    private static final int EXISTING_BOOK_ID = 1;
    private static final String EXISTING_BOOK_TITLE = "lovers of death";
    private static final String EXISTING_BOOK_TEXT = "text of the book with name <<lovers of death>>";
    private static final int EXISTING_AUTHOR_ID = 1;
    private static final String EXISTING_AUTHOR_NAME = "Boris Akunin";
    private static final int EXISTING_GENRE_ID = 1;
    private static final String EXISTING_GENRE_NAME = "detective";

    @Mock
    private BookDao dao;
    @Mock
    private GenreDao genreDao;
    @Mock
    private AuthorDao authorDao;
    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @BeforeEach
    void setUp(){
        bookServiceImpl = new BookServiceImpl(dao, genreDao, authorDao);
    }

    @DisplayName("должен находить книгу по названию")
    @Test
    void shouldFindBookByTitle(){
        Book book = Book.builder().id(EXISTING_BOOK_ID).title(EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT)
                .author(Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build())
                .genre(Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build()).build();
        given(dao.findByTitle(EXISTING_BOOK_TITLE)).willReturn(book);
        assertEquals(book, bookServiceImpl.findByTitle(EXISTING_BOOK_TITLE));
    }

    @DisplayName("должен находить все книги")
    @Test
    void shouldFindAllBooks(){
        List<Book> books = List.of(Book.builder().id(EXISTING_BOOK_ID).title(EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT)
                .author(Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build())
                .genre(Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build()).build());
        given(dao.findAll()).willReturn(books);
        assertEquals(books, bookServiceImpl.findAll());
    }

    @DisplayName("должен удалять книгу по имени")
    @Test
    void shouldDeleteBookByName(){
        doThrow(new BookNotFoundException("book with title " + EXISTING_BOOK_TITLE + " was not found"))
                .when(dao).deleteByTitle(EXISTING_BOOK_TITLE);
        assertThrows(BookNotFoundException.class,
                () -> bookServiceImpl.deleteByTitle(EXISTING_BOOK_TITLE));
    }

    @DisplayName("должен добавлять автора")
    @Test
    void shouldInsertBook(){
        Author author = Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build();
        Genre genre = Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build();
        Book book = Book.builder().id(EXISTING_BOOK_ID).title(EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT)
                .author(author)
                .genre(genre).build();
        given(authorDao.findByName(EXISTING_AUTHOR_NAME)).willReturn(author);
        given(genreDao.findByName(EXISTING_GENRE_NAME)).willReturn(genre);
        given(dao.findByTitle(book.getTitle())).willThrow(BookNotFoundException.class);
        given(dao.insert(book)).willReturn(book);
        assertEquals(bookServiceImpl.insert(book), book);
    }
}
