package org.example.service;

import org.example.dao.AuthorDao;
import org.example.domain.Author;
import org.example.exception.AuthorAlreadyExistsException;
import org.example.exception.AuthorNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Класс AuthorService")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class AuthorServiceTest {

    private static final String EXISTING_AUTHOR_NAME = "Boris Akunin";
    private static final long EXISTING_AUTHOR_ID = 1;
    private static final String EXISTING_AUTHOR_NAME2 = "Leach23";
    private static final long EXISTING_AUTHOR_ID2 = 2;
    private static final String NEW_AUTHOR_NAME = "newAuthorName";

    @Mock
    private AuthorDao dao;

    private AuthorService authorService;

    private final List<Author> authors = new ArrayList<>();

    @BeforeEach
    void setUp(){
        authorService = new AuthorServiceImpl(dao);
        authors.clear();
        Author author1 = Author.builder().id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build();
        Author author2 = Author.builder().id(EXISTING_AUTHOR_ID2).name(EXISTING_AUTHOR_NAME2).build();
        authors.add(author1);
        authors.add(author2);
    }

    @DisplayName("должен найти автора по имени")
    @Test
    void shouldFindAuthorByName(){
        given(dao.findByName(EXISTING_AUTHOR_NAME)).willReturn(Optional.of(authors.get(0)));
        assertEquals(authors.get(0), authorService.findByName(EXISTING_AUTHOR_NAME));
    }

    @DisplayName("должен найти всех авторов")
    @Test
    void shouldFindAllAuthors(){
        given(dao.findAll()).willReturn(authors);
        assertEquals(authors, authorService.findAll());
    }

    @DisplayName("должен удалить автора по имени")
    @Test
    void shouldDeleteAuthorByName(){
        given(dao.findByName(EXISTING_AUTHOR_NAME)).willReturn(Optional.of(authors.get(0)));
        authorService.deleteByName(EXISTING_AUTHOR_NAME);
        assertAll(
                () -> verify(dao, times(1)).deleteByName(any()),
                () -> assertThrows(AuthorNotFoundException.class, () -> authorService.deleteByName(NEW_AUTHOR_NAME))
        );
    }

    @DisplayName("должен добавить автора")
    @Test
    void shouldInsertAuthor(){
        Author author = Author.builder().name(NEW_AUTHOR_NAME).build();
        given(dao.insert(author)).willReturn(author);
        assertAll(
                () -> assertEquals(authorService.insert(author).getName(), author.getName()),
                () -> assertThrows(AuthorAlreadyExistsException.class, () -> authorService.insert(
                        authors.get(0)))
        );
    }
}
