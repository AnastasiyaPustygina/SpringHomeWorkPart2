package org.example.dao;

import java.util.List;
import org.junit.jupiter.api.*;
import org.example.domain.Author;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;




@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Класс AuthorDao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorDaoTest {

    private static final String EXISTING_AUTHOR_NAME = "Boris Akunin";
    private static final String NEW_AUTHOR_NAME = "Thomas Harris";
    private static final long COUNT_OF_AUTHORS = 3;

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("должен найти автора по имени")
    void shouldFindAuthorByName(){
        assertThat(authorDao.findByName(EXISTING_AUTHOR_NAME)).isPresent().get()
                .hasFieldOrPropertyWithValue("name", EXISTING_AUTHOR_NAME);
    }

    @Test
    @DisplayName("должен найти всех авторов")
    void shouldFindAllAuthors(){
        List<Author> authors = authorDao.findAll();
        assertEquals(authors.size(), COUNT_OF_AUTHORS);
    }
    @Test
    @DisplayName("должен добавить автора")
    void shouldInsertAuthor(){
        long countBefore = authorDao.findAll().size();
        Author author = Author.builder().name(NEW_AUTHOR_NAME).build();
        assertAll(
                () -> assertEquals(NEW_AUTHOR_NAME, authorDao.save(author).getName()),
                () -> assertEquals(countBefore + 1, authorDao.findAll().size())
        );
    }
    @Test
    @DisplayName("должен удалять автора по имени")
    void shouldDeleteAuthorByName(){
        long countBefore = authorDao.findAll().size();
        authorDao.deleteByName(EXISTING_AUTHOR_NAME);
        assertAll(
                () -> assertEquals(countBefore - 1, authorDao.findAll().size()),
                () -> assertTrue(authorDao.findByName(EXISTING_AUTHOR_NAME).isEmpty())
        );
    }
}
