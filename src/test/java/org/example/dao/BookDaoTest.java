package org.example.dao;

import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Genre;
import org.example.exception.BookNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({AuthorDaoImpl.class, BookDaoImpl.class, GenreDaoImpl.class})
@DisplayName("Класс BookDao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BookDaoTest {

    private static final int EXISTING_BOOK_ID = 1;
    private static final String EXISTING_BOOK_TITLE = "lovers of death";
    private static final String EXISTING_BOOK_TEXT = "text of the book with name <<lovers of death>>";
    private static final int EXISTING_AUTHOR_ID = 1;
    private static final String EXISTING_AUTHOR_NAME = "Boris Akunin";
    private static final int EXISTING_GENRE_ID = 1;
    private static final String EXISTING_GENRE_NAME = "detective";
    private static final List<Book> INITIAL_LIST = List.of(Book.builder().id(EXISTING_BOOK_ID).title(
            EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT).author(Author.builder().id(EXISTING_AUTHOR_ID).name(
                    EXISTING_AUTHOR_NAME).build()).genre(Genre.builder().id(EXISTING_GENRE_ID).name(
                            EXISTING_GENRE_NAME).build()).build());
    @Autowired
    private BookDao bookDao;

    private List<Book> initialList;

    @BeforeEach
    void resetChanges(){
        bookDao.setBooks(INITIAL_LIST);
    }

    @Test
    @DisplayName(value = "должен искать книгу по названию")
    void shouldFindBookByTitle(){
        Book book = Book.builder().id(EXISTING_BOOK_ID).title(EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT)
                .author(Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build())
                .genre(Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build()).build();
        assertEquals(bookDao.findByTitle(EXISTING_BOOK_TITLE), book);
    }

    @Test
    @DisplayName(value = "должен искать все книги")
    void soundFindAllBooks(){
        List<Book> books = List.of(Book.builder().id(EXISTING_BOOK_ID).title(EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT)
                .author(Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build())
                .genre(Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build()).build());
        assertThat(bookDao.findAll()).usingRecursiveComparison().isEqualTo(books);
    }

    @Test
    @DisplayName(value = "должен добавлять книгу")
    void shouldInsertBook(){
        long count_before = bookDao.findAll().size();
        Book book = Book.builder().id(2).title("the player who climbed to the top").text(
                "text of the book with title<<the player who climbed to the top>>").author(Author.builder()
                        .id(2).name("Leach23").build()).genre(Genre.builder().id(2).name("fantastic").build()).build();
        Book resultingBook = bookDao.insert(book);
        assertAll(
                () -> assertThat(resultingBook).usingRecursiveComparison().ignoringFields("id", "author.id", "genre.id")
                        .isEqualTo(book),
                () -> assertEquals(count_before + 1, bookDao.findAll().size())
        );
    }
    @Test
    @DisplayName(value = "должен удалять книгу по названию")
    void shouldDeleteBookByTitle(){
        int countBefore = bookDao.findAll().size();
        bookDao.deleteByTitle(EXISTING_BOOK_TITLE);
        assertAll(
                () -> assertThrows(BookNotFoundException.class, () -> bookDao.findByTitle(EXISTING_BOOK_TITLE)),
                () -> assertEquals(countBefore - 1, bookDao.findAll().size())
        );
    }

    @Test
    @DisplayName(value = "должен удалять книгу по id автора")
    void shouldDeleteBookByAuthorId(){
        assertDoesNotThrow(() -> bookDao.findByTitle(EXISTING_BOOK_TITLE));
        bookDao.deleteBooksByAuthorId(EXISTING_AUTHOR_ID);
        assertThrows(BookNotFoundException.class, () -> bookDao.findByTitle(EXISTING_BOOK_TITLE));
    }

    @Test
    @DisplayName(value = "должен удалять книгу по id жанра")
    void shouldDeleteBookByGenreId(){
        assertDoesNotThrow(() -> bookDao.findByTitle(EXISTING_BOOK_TITLE));
        bookDao.deleteBooksByGenreId(EXISTING_GENRE_ID);
        assertThrows(BookNotFoundException.class, () -> bookDao.findByTitle(EXISTING_BOOK_TITLE));
    }

}
