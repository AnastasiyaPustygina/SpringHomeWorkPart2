package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Comment;
import org.example.domain.Genre;
import org.example.rest.dto.CommentDto;
import org.example.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("класс CommentController")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentControllerTest {

    private static final String EXISTING_BOOK_TITLE = "Lovers of death";
    private static final long EXISTING_BOOK_ID = 1;
    private static final String EXISTING_BOOK_TEXT = "text of the book with name <<Lovers of death>>";
    private static final long EXISTING_AUTHOR_ID = 1;
    private static final String EXISTING_AUTHOR_NAME = "Boris Akunin";
    private static final long EXISTING_GENRE_ID = 1;
    private static final String EXISTING_GENRE_NAME = "detective";
    private static final long EXISTING_COMMENT_ID = 1;
    private static final String EXISTING_COMMENT_TEXT = "Content of first comment";
    private static final String NEW_COMMENT_TEXT = "Content of new comment";
    private static final int COUNT_OF_COMMENTS = 3;
    private static final int COUNT_OF_FIRST_BOOK_COMMENTS = 2;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CommentService commentService;

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    @DisplayName("должен найти все комментарии")
    void shouldFindAllComments() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/comment/")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(COUNT_OF_COMMENTS)));
    }

    @WithMockUser(username = "reader", authorities = {"ROLE_READER"})
    @Test
    @DisplayName("должен найти комментарий по id")
    void shouldFindCommentById() throws Exception {
        Comment comment = Comment.builder().id(EXISTING_COMMENT_ID).text(EXISTING_COMMENT_TEXT).book(Book.builder().id(EXISTING_BOOK_ID)
                .title(EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT).author(Author.builder()
                        .id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build()).genre(Genre.builder()
                        .id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build()).comments(Collections.emptyList()).build()).build();
        mvc.perform(MockMvcRequestBuilders.get("/comment/" + EXISTING_COMMENT_ID)).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(CommentDto.toDto(comment))));
    }

    @WithMockUser(username = "reader", authorities = {"ROLE_READER"})
    @Test
    @DisplayName("должен найти комментарии по названию книги")
    void shouldFindCommentsByBookTitle() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/comment/book/" + EXISTING_BOOK_TITLE))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(COUNT_OF_FIRST_BOOK_COMMENTS)));
    }

    @WithMockUser(username = "reader", authorities = {"ROLE_READER"})
    @Test
    @DisplayName("должен добавить комментарий")
    void shouldInsertComment() throws Exception {
        Comment comment = Comment.builder().text(NEW_COMMENT_TEXT).book(Book.builder().id(EXISTING_BOOK_ID)
                .title(EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT).author(Author.builder()
                        .id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build()).genre(Genre.builder()
                        .id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build()).comments(Collections.emptyList()).build()).build();
        mvc.perform(MockMvcRequestBuilders.post("/comment").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(CommentDto.toDto(comment)))
                .param("bookTitle", EXISTING_BOOK_TITLE)).andExpect(status().isOk());
    }

    @WithMockUser(username = "reader", authorities = {"ROLE_READER"})
    @Test
    @DisplayName("должен изменить текст комментария по id")
    void shouldUpdateCommentTextById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch("/comment/" + EXISTING_COMMENT_ID  + "/text").param(
                "text", NEW_COMMENT_TEXT)).andExpect(status().isOk());
    }


}
