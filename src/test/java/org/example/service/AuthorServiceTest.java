package org.example.service;

import org.example.dao.AuthorDao;
import org.example.domain.Author;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@DisplayName("Класс AuthorService")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class AuthorServiceTest {

    private static final String EXISTING_AUTHOR_NAME = "Boris Akunin";
    private static final int EXISTING_AUTHOR_ID = 1;
    public static final String NEW_AUTHOR_NAME = "newAuthorName";

    @Mock
    private AuthorDao dao;
    @InjectMocks
    private AuthorServiceImpl authorServiceImpl;

    @BeforeEach
    void setUp(){
        authorServiceImpl = new AuthorServiceImpl(dao);
    }

    @DisplayName("должен найти автора по имени")
    @Test
    void shouldFindAuthorByName(){
        Author author = Author.builder().id(EXISTING_AUTHOR_ID)
                .name(EXISTING_AUTHOR_NAME).build();
        given(dao.findByName(EXISTING_AUTHOR_NAME)).willReturn(author);
        assertEquals(author, authorServiceImpl.findByName(EXISTING_AUTHOR_NAME));
    }

    @DisplayName("должен найти всех авторов")
    @Test
    void shouldFindAllAuthors(){
        List<Author> authors = List.of(Author.builder().id(EXISTING_AUTHOR_ID)
                .name(EXISTING_AUTHOR_NAME).build());
        given(dao.findAll()).willReturn(authors);
        System.out.println(authorServiceImpl.findAll());
        assertEquals(authors, authorServiceImpl.findAll());
    }

    @DisplayName("должен удалять автора по имени")
    @Test
    void shouldDeleteAuthorByName(){
        doThrow(new AuthorNotFoundException("author with name " + EXISTING_AUTHOR_NAME + " was not found"))
                .when(dao).deleteByName(EXISTING_AUTHOR_NAME);
        assertThrows(AuthorNotFoundException.class,
                () -> authorServiceImpl.deleteByName(EXISTING_AUTHOR_NAME));
    }

    @DisplayName("должен добавлять автора")
    @Test
    void shouldInsertAuthor(){
        Author author = Author.builder().name(NEW_AUTHOR_NAME).build();
        given(dao.findByName(author.getName())).willThrow(AuthorNotFoundException.class);
        given(dao.insert(author)).willReturn(author);
        assertEquals(authorServiceImpl.insert(author), author);
    }

}
