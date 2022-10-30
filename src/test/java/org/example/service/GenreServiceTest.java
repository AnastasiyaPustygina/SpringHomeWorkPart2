package org.example.service;

import java.util.List;
import org.mockito.Mock;
import java.util.Optional;
import java.util.ArrayList;
import org.example.domain.Genre;
import org.example.dao.GenreDao;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.exception.GenreNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.example.exception.GenreAlreadyExistsException;

@DisplayName("Класс GenreService")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class GenreServiceTest {

    private static final String EXISTING_GENRE_NAME = "detective";
    private static final int EXISTING_GENRE_ID = 1;
    private static final String EXISTING_GENRE_NAME2 = "fantastic";
    private static final int EXISTING_GENRE_ID2 = 2;
    public static final String NEW_GENRE_NAME = "newGenreName";

    @Mock
    private GenreDao dao;

    private GenreService genreService;

    private final List<Genre> genres = new ArrayList<>();

    @BeforeEach
    void setUp(){
        genreService = new GenreServiceImpl(dao);
        genres.clear();
        Genre genre1 = Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build();
        Genre genre2 = Genre.builder().id(EXISTING_GENRE_ID2).name(EXISTING_GENRE_NAME2).build();
        genres.add(genre1);
        genres.add(genre2);
    }

    @DisplayName("должен найти жанр по имени")
    @Test
    void shouldFindGenreByName(){
        given(dao.findByName(EXISTING_GENRE_NAME)).willReturn(Optional.of(genres.get(0)));
        assertEquals(genres.get(0), genreService.findByName(EXISTING_GENRE_NAME));
    }

    @DisplayName("должен найти все жанры")
    @Test
    void shouldFindAllGenres(){
        given(dao.findAll()).willReturn(genres);
        assertEquals(genres, genreService.findAll());
    }

    @DisplayName("должен удалить жанр по имени")
    @Test
    void shouldDeleteGenreByName(){
        given(dao.findByName(EXISTING_GENRE_NAME)).willReturn(Optional.of(genres.get(0)));
        genreService.deleteByName(EXISTING_GENRE_NAME);
        assertAll(
                () -> verify(dao, times(1)).deleteByName(any()),
                () -> assertThrows(GenreNotFoundException.class, () -> genreService.deleteByName(NEW_GENRE_NAME))
        );
    }

    @DisplayName("должен добавить жанр")
    @Test
    void shouldInsertGenre(){
        Genre genre = Genre.builder().name(NEW_GENRE_NAME).build();
        given(dao.save(any())).willReturn(genre);
        given(dao.findByName(EXISTING_GENRE_NAME)).willReturn(Optional.of(genres.get(0)));
        given(dao.findByName(NEW_GENRE_NAME)).willReturn(Optional.empty());
        assertAll(
                () -> assertEquals(genreService.insert(genre).getName(), genre.getName()),
                () -> assertThrows(GenreAlreadyExistsException.class, () -> {
                    genreService.insert(Genre.builder().name(EXISTING_GENRE_NAME).build());
                })
        );
    }
}


