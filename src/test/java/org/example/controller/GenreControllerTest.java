package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.Genre;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.exception.GenreNotFoundException;
import org.example.rest.dto.GenreDto;
import org.example.service.GenreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Класс GenreController")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreControllerTest {
    private static final String EXISTING_GENRE_NAME = "detective";
    private static final long EXISTING_GENRE_ID = 1;
    private static final String EXISTING_GENRE_NAME2 = "fantastic";
    private static final long EXISTING_GENRE_ID2 = 2;
    private static final String EXISTING_GENRE_NAME3 = "adventure";
    private static final long EXISTING_GENRE_ID3 = 3;

    private static final String NEW_GENRE_NAME = "newGenreName";
    private static final long NEW_GENRE_ID = 4;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private GenreService genreService;

    @Test
    @DisplayName("должен найти все жанры")
    void shouldFindAllGenres() throws Exception {
        Genre genre1 = Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build();
        Genre genre2 = Genre.builder().id(EXISTING_GENRE_ID2).name(EXISTING_GENRE_NAME2).build();
        Genre genre3 = Genre.builder().id(EXISTING_GENRE_ID3).name(EXISTING_GENRE_NAME3).build();
        List<GenreDto> genres = Stream.of(genre1, genre2, genre3).map(GenreDto::toDto).toList();
        mvc.perform(MockMvcRequestBuilders.get("/genre/"))
                .andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(genres)));
    }

    @Test
    @DisplayName("должен найти жанр по имени")
    void shouldFindGenreByName() throws Exception {
        Genre genre = Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build();
        mvc.perform(MockMvcRequestBuilders.get("/genre/" + EXISTING_GENRE_NAME))
                .andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(GenreDto.toDto(genre))));
    }

    @Test
    @DisplayName("должен добавить жанр")
    void shouldInsertGenre() throws Exception {
        Genre genre = Genre.builder().name(NEW_GENRE_NAME).build();
        mvc.perform(MockMvcRequestBuilders.post("/genre").contentType(APPLICATION_JSON).content(
                        mapper.writeValueAsString(GenreDto.toDto(genre)))).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(GenreDto.toDto(Genre.builder().
                        id(NEW_GENRE_ID).name(NEW_GENRE_NAME).build()))));
    }

    @Test
    @DisplayName("должен удалять жанр по имени")
    void shouldDeleteGenreByName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/genre/" + EXISTING_GENRE_NAME)).andExpect(status().isOk());
        assertThrows(GenreNotFoundException.class, () -> genreService.findByName(EXISTING_GENRE_NAME));
    }

}
