package org.example.dao;

import org.example.domain.Author;
import org.example.exception.AuthorNotFoundException;
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
@Import({AuthorDaoImpl.class})
@DisplayName("Класс AuthorDao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AuthorDaoTest {

    private static final String EXISTING_AUTHOR_NAME = "Boris Akunin";
    private static final int EXISTING_AUTHOR_ID = 1;
    private static final List<Author> INITIAL_LIST =
            List.of(Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build());
    @Autowired
    private AuthorDao authorDao;


    @BeforeEach
    void resetChanges(){
        authorDao.setAuthors(INITIAL_LIST);
    }

    @Test
    @DisplayName("должен найти автора по имени")
    void shouldFindAuthorByName(){
        Author author = Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build();
        assertEquals(author, authorDao.findByName(EXISTING_AUTHOR_NAME));
    }

    @Test
    @DisplayName("должен найти всех авторов")
    void shouldFindAllAuthors(){
        List<Author> authors = List.of(Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build());
        List<Author> resultingAuthor = authorDao.findAll();
        assertThat(resultingAuthor).usingRecursiveComparison().isEqualTo(authors);
    }
    @Test
    @DisplayName("должен добавить автора")
    void shouldInsertAuthor(){
        long countBefore = authorDao.findAll().size();
        Author author = Author.builder().id(2).name("Leach23").build();
        assertAll(
                () -> assertEquals(author.getName(), authorDao.insert(author).getName()),
                () -> assertEquals(countBefore + 1, authorDao.findAll().size())
        );
    }
    @Test
    @DisplayName("должен удалять автора по имени")
    void shouldDeleteAuthorByName(){
        int countBefore = authorDao.findAll().size();
        authorDao.deleteByName(EXISTING_AUTHOR_NAME);
        assertAll(
                () -> assertEquals(countBefore - 1, authorDao.findAll().size()),
                () -> assertThrows(AuthorNotFoundException.class, () -> authorDao.findByName(EXISTING_AUTHOR_NAME))
        );
    }
}
