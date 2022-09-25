package org.example.dao;

import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Genre;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Класс BookDao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BookDaoTest {

    private static final String EXISTING_BOOK_TITLE = "Lovers of death";
    private static final String NEW_BOOK_TEXT = "text of the book with title<<the player who climbed to the top>>";
    private static final long EXISTING_AUTHOR_ID = 2;
    private static final String EXISTING_AUTHOR_NAME = "Leach23";
    private static final String NEW_BOOK_TITLE = "The player who climbed to the top";
    private static final long EXISTING_GENRE_ID = 2;
    private static final String EXISTING_GENRE_NAME = "fantastic";
    private static final long COUNT_OF_BOOK = 3;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName(value = "должен искать книгу по названию")
    void shouldFindBookByTitle(){
        assertThat(bookDao.findByTitle(EXISTING_BOOK_TITLE)).isPresent().get()
                .hasFieldOrPropertyWithValue("title", EXISTING_BOOK_TITLE);
    }

    @Test
    @DisplayName(value = "должен искать все книги")
    void soundFindAllBooks(){
        assertEquals(bookDao.findAll().size(), COUNT_OF_BOOK);
    }

    @Test
    @DisplayName(value = "должен добавлять книгу")
    void shouldInsertBook(){
        long count_before = bookDao.findAll().size();
        Genre genre = entityManager.merge(Genre.builder()
                .id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build());
        Author author = entityManager.merge(Author.builder()
                .id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build());
        Book book = Book.builder().title(NEW_BOOK_TITLE).text(NEW_BOOK_TEXT).author(author)
                .genre(genre).comments(Collections.emptyList()).build();
        bookDao.save(book);
        assertAll(
                () -> assertEquals(book.getTitle(), book.getTitle()),
                () -> assertEquals(count_before + 1, bookDao.findAll().size())
        );
    }
    @Test
    @DisplayName(value = "должен удалять книгу по названию")
    void shouldDeleteBookByTitle(){
        int countBefore = bookDao.findAll().size();
        bookDao.deleteByTitle(EXISTING_BOOK_TITLE);
        assertAll(
                () -> assertTrue(bookDao.findByTitle(EXISTING_BOOK_TITLE).isEmpty()),
                () -> assertEquals(countBefore - 1, bookDao.findAll().size())
        );
    }

}
