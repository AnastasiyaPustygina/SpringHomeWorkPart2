package org.example.dao;

import org.example.domain.Author;
import org.example.domain.Genre;
import org.example.exception.GenreNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({AuthorDaoImpl.class, BookDaoImpl.class, GenreDaoImpl.class})
@DisplayName("Класс GenreDao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class GenreDaoTest {

    private static final String EXISTING_GENRE_NAME = "detective";
    private static final int EXISTING_GENRE_ID = 1;
    private static final List<Genre> INITIAL_LIST =
            List.of(Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build());

    @Autowired
    private GenreDao genreDao;

    private List<Genre> initialList;

    @BeforeEach
    void resetChanges(){
        genreDao.setGenres(INITIAL_LIST);
    }

    @Test
    @DisplayName("должен найти жанр по имени")
    void shouldFindGenreByName(){
        Genre genre = Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build();
        assertEquals(genre, genreDao.findByName(EXISTING_GENRE_NAME));
    }

    @Test
    @DisplayName("должен найти все жанры")
    void shouldFindAllGenres(){
        List<Genre> genres = List.of(Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build());
        List<Genre> resultingGenre = genreDao.findAll();
        assertThat(resultingGenre).usingRecursiveComparison().isEqualTo(genres);
    }
    @Test
    @DisplayName("должен добавить жанр")
    void shouldInsertGenre(){
        long countBefore = genreDao.findAll().size();
        Genre genre = Genre.builder().id(2).name("fantastic").build();
        assertAll(
                () -> assertEquals(genre.getName(), genreDao.insert(genre).getName()),
                () -> assertEquals(countBefore + 1, genreDao.findAll().size())
        );
    }
    @Test
    @DisplayName("должен удалять жанр по имени")
    void shouldDeleteGenreByName(){
        int countBefore = genreDao.findAll().size();
        genreDao.deleteByName(EXISTING_GENRE_NAME);
        assertAll(
                () -> assertEquals(countBefore - 1, genreDao.findAll().size()),
                () -> assertThrows(GenreNotFoundException.class, () -> genreDao.findByName(EXISTING_GENRE_NAME))
        );
    }
}
