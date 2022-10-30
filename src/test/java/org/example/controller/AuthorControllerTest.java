package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.Author;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.exception.AuthorNotFoundException;
import org.example.rest.dto.AuthorDto;
import org.example.service.AuthorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Класс AuthorController")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthorControllerTest {

    private static final String EXISTING_AUTHOR_NAME = "Boris Akunin";
    private static final long EXISTING_AUTHOR_ID = 1;
    private static final String EXISTING_AUTHOR_NAME2 = "Leach23";
    private static final long EXISTING_AUTHOR_ID2 = 2;
    private static final String EXISTING_AUTHOR_NAME3 = "Agatha Christie";
    private static final long EXISTING_AUTHOR_ID3 = 3;

    private static final String NEW_AUTHOR_NAME = "newAuthorName";
    private static final long NEW_AUTHOR_ID = 4;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AuthorService authorService;

    @WithMockUser(username = "reader", authorities = {"ROLE_READER"})
    @Test
    @DisplayName("должен найти всех авторов")
    void shouldFindAllAuthors() throws Exception {
        Author author1 = Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build();
        Author author2 = Author.builder().id(EXISTING_AUTHOR_ID2).name(EXISTING_AUTHOR_NAME2).build();
        Author author3 = Author.builder().id(EXISTING_AUTHOR_ID3).name(EXISTING_AUTHOR_NAME3).build();
        List<AuthorDto> authors = Stream.of(author1, author2, author3).map(AuthorDto::toDto).toList();
        mvc.perform(MockMvcRequestBuilders.get("/author/"))
                .andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(authors)));
    }

    @WithMockUser(username = "reader", authorities = {"ROLE_READER"})
    @Test
    @DisplayName("должен найти автора по имени")
    void shouldFindAuthorByName() throws Exception {
        Author author = Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build();
        mvc.perform(MockMvcRequestBuilders.get("/author/" + EXISTING_AUTHOR_NAME))
                .andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(AuthorDto.toDto(author))));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    @DisplayName("должен добавить автора")
    void shouldInsertAuthor() throws Exception {
        Author author = Author.builder().name(NEW_AUTHOR_NAME).build();
        mvc.perform(MockMvcRequestBuilders.post("/author").contentType(APPLICATION_JSON).content(
                mapper.writeValueAsString(AuthorDto.toDto(author)))).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(AuthorDto.toDto(Author.builder().id(NEW_AUTHOR_ID)
                        .name(author.getName()).build()))));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    @DisplayName("должен удалять автора по имени")
    void shouldDeleteAuthorByName() throws Exception {
        System.out.println(authorService.findAll());
        mvc.perform(MockMvcRequestBuilders.delete("/author/" + EXISTING_AUTHOR_NAME)).andExpect(status().isOk());
        assertThrows(AuthorNotFoundException.class, () -> authorService.findByName(EXISTING_AUTHOR_NAME));
    }

}
