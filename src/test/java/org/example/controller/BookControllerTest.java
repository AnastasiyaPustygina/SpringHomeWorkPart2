package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Comment;
import org.example.domain.Genre;
import org.example.exception.BookNotFoundException;
import org.example.rest.dto.BookDto;
import org.example.service.BookService;
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

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@DisplayName("класс BookController")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookControllerTest {

    private static final long EXISTING_BOOK_ID = 1;
    private static final String EXISTING_BOOK_TITLE = "Lovers of death";
    private static final String EXISTING_BOOK_TEXT = "text of the book with title<<Lovers of death>>";
    private static final String NEW_BOOK_TEXT = "text of the book with title<<newBookTitle>>";
    private static final long EXISTING_AUTHOR_ID = 1;
    private static final String EXISTING_AUTHOR_NAME = "Boris Akunin";
    private static final String NEW_BOOK_TITLE = "newBookTitle";
    private static final long EXISTING_GENRE_ID = 1;
    private static final String EXISTING_GENRE_NAME = "detective";
    private static final long FIRST_COMMENT_ID = 1;
    private static final String FIRST_COMMENT_TEXT = "Content of first comment";
    private static final long SECOND_COMMENT_ID = 2;
    private static final String SECOND_COMMENT_TEXT = "Content of second comment";
    private static final int COUNT_OF_BOOK = 3;
    private static final long NEW_BOOK_ID = 4;

    @Autowired
    private CommentService commentService;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookService bookService;

    @WithMockUser(username = "reader", authorities = {"ROLE_READER"})
    @Test
    @DisplayName("должен найти все книги")
    public void shouldFindAllBooks() throws Exception {
        mvc.perform(get("/book/")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(COUNT_OF_BOOK)));
    }

    @WithMockUser(username = "reader", authorities = {"ROLE_READER"})
    @Test
    @DisplayName("должен найти книгу по названию")
    void shouldFindBookByTitle() throws Exception {
        Genre genre = Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build();
        Author author = Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build();
        List<Comment> comments = List.of(Comment.builder().id(FIRST_COMMENT_ID).text(FIRST_COMMENT_TEXT).build(),
                Comment.builder().id(SECOND_COMMENT_ID).text(SECOND_COMMENT_TEXT).build());
        Book book = Book.builder().id(EXISTING_BOOK_ID).title(EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT).author(author)
                .genre(genre).comments(comments).build();
        BookDto bookDto = BookDto.toDto(book);
        System.out.println(commentService.findByBookTitle(EXISTING_BOOK_TITLE));
        mvc.perform(get("/book/" + EXISTING_BOOK_TITLE))
                .andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(bookDto)));
    }

    @WithMockUser(username = "author", authorities = {"ROLE_AUTHOR"})
    @Test
    @DisplayName("должен добавить книгу")
    void shouldInsertBook() throws Exception {
        Genre genre = Genre.builder().id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build();
        Author author = Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build();
        BookDto bookDto = BookDto.toDto(Book.builder().title(NEW_BOOK_TITLE).text(NEW_BOOK_TEXT).author(author)
                .genre(genre).comments(Collections.emptyList()).build());
        BookDto resultingBookDto = BookDto.toDto(Book.builder().id(NEW_BOOK_ID).title(NEW_BOOK_TITLE).text(NEW_BOOK_TEXT).author(author)
                .genre(genre).comments(Collections.emptyList()).build());
        mvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto))).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(resultingBookDto)));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    @DisplayName("должен удалить книгу по имени")
    void shouldDeleteBookByTitle() throws Exception{
        mvc.perform(delete("/book/" + EXISTING_BOOK_TITLE)).andExpect(status().isOk());
        assertThrows(BookNotFoundException.class, () -> bookService.findByTitle(EXISTING_BOOK_TITLE));
    }

}
