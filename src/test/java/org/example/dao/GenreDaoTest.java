package org.example.dao;

import org.junit.jupiter.api.*;
import org.example.domain.Genre;
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
@DisplayName("Класс GenreDao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GenreDaoTest {

    private static final String EXISTING_GENRE_NAME = "detective";
    private static final String NEW_GENRE_NAME = "newGenreName";
    private static final long COUNT_OF_GENRES = 3;

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("должен найти жанр по имени")
    void shouldFindGenreByName(){
        assertThat(genreDao.findByName(EXISTING_GENRE_NAME)).isPresent().get()
                .hasFieldOrPropertyWithValue("name", EXISTING_GENRE_NAME);
    }

    @Test
    @DisplayName("должен найти все жанры")
    void shouldFindAllGenres(){
        assertEquals(genreDao.findAll().size(), COUNT_OF_GENRES);
    }
    @Test
    @DisplayName("должен добавить жанр")
    void shouldInsertGenre(){
        long countBefore = genreDao.findAll().size();
        Genre genre = Genre.builder().name(NEW_GENRE_NAME).build();
        assertAll(
                () -> assertEquals(genre.getName(), genreDao.save(genre).getName()),
                () -> assertEquals(countBefore + 1, genreDao.findAll().size())
        );
    }
    @Test
    @DisplayName("должен удалять жанр по имени")
    void shouldDeleteGenreByName(){
        long countBefore = genreDao.findAll().size();
        genreDao.deleteByName(EXISTING_GENRE_NAME);
        assertAll(
                () -> assertEquals(countBefore - 1, genreDao.findAll().size()),
                () -> assertTrue(genreDao.findByName(EXISTING_GENRE_NAME).isEmpty())
        );
    }
}
