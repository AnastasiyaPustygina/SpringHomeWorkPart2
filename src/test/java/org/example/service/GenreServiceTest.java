package org.example.service;

import org.example.dao.GenreDao;
import org.example.domain.Genre;
import org.example.exception.GenreNotFoundException;
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

@DisplayName("Класс GenreService")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class GenreServiceTest {
    private static final String EXISTING_GENRE_NAME = "detective";
    private static final int EXISTING_GENRE_ID = 1;

    @Mock
    private GenreDao dao;
    @InjectMocks
    private GenreServiceImpl genreServiceImpl;

    @BeforeEach
    void setUp(){
        genreServiceImpl = new GenreServiceImpl(dao);
    }

    @DisplayName("должен найти жанр по имени")
    @Test
    void shouldFindGenreByName(){
        Genre genre = Genre.builder().id(EXISTING_GENRE_ID)
                .name(EXISTING_GENRE_NAME).build();
        given(dao.findByName(EXISTING_GENRE_NAME)).willReturn(genre);
        assertEquals(genre, genreServiceImpl.findByName(EXISTING_GENRE_NAME));
    }

    @DisplayName("должен найти все жанры")
    @Test
    void shouldFindAllGenres(){
        List<Genre> genres = List.of(Genre.builder().id(EXISTING_GENRE_ID)
                .name(EXISTING_GENRE_NAME).build());
        given(dao.findAll()).willReturn(genres);
        System.out.println(genreServiceImpl.findAll());
        assertEquals(genres, genreServiceImpl.findAll());
    }

    @DisplayName("должен удалять жанр по имени")
    @Test
    void shouldDeleteGenreByName(){
        doThrow(new GenreNotFoundException("genre with name " + EXISTING_GENRE_NAME + " was not found"))
                .when(dao).deleteByName(EXISTING_GENRE_NAME);
        assertThrows(GenreNotFoundException.class,
                () -> genreServiceImpl.deleteByName(EXISTING_GENRE_NAME));
    }

    @DisplayName("должен добавлять жанр")
    @Test
    void shouldInsertGenre(){
        Genre genre = Genre.builder().id(EXISTING_GENRE_ID)
                .name(EXISTING_GENRE_NAME).build();
        given(dao.insert(genre)).willReturn(genre);
        assertEquals(genreServiceImpl.insert(genre), genre);
    }
}


