package org.example.service;

import org.example.dao.BookDao;
import org.example.dao.CommentDao;
import org.example.domain.Book;
import org.example.domain.Comment;
import org.example.exception.CommentAlreadyExistsException;
import org.example.exception.CommentNotFoundException;
import org.example.rest.dto.CommentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Класс CommentService")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CommentServiceTest {

    private static final String EXISTING_COMMENT_TEXT = "Content of first comment";
    private static final long EXISTING_COMMENT_ID = 1;
    private static final long EXISTING_BOOK_ID = 1;
    private static final String EXISTING_BOOK_TITLE = "Lovers of death";
    private static final String EXISTING_COMMENT_TEXT2 = "Content of second comment";
    private static final long EXISTING_COMMENT_ID2 = 2;
    private static final String NEW_COMMENT_TEXT = "Content of new comment";
    private static final long NEW_COMMENT_ID = 3;


    @Mock
    private CommentDao commentDao;
    @Mock
    private BookDao bookDao;

    private CommentService commentService;

    private final List<Comment> comments = new ArrayList<>();

    @BeforeEach
    void setUp(){
        commentService = new CommentServiceImpl(commentDao,bookDao);
        comments.clear();
        Comment comment1 = Comment.builder().id(EXISTING_COMMENT_ID).text(EXISTING_COMMENT_TEXT).build();
        Comment comment2 = Comment.builder().id(EXISTING_COMMENT_ID2).text(EXISTING_COMMENT_TEXT2).build();
        comments.add(comment1);
        comments.add(comment2);
    }

    @DisplayName("должен найти комментарий по id")
    @Test
    void shouldFindCommentById(){
        given(commentDao.findById(EXISTING_COMMENT_ID)).willReturn(Optional.of(comments.get(0)));
        assertEquals(comments.get(0), commentService.findById(EXISTING_COMMENT_ID));
    }

    @DisplayName("должен найти комментарий по названию книги")
    @Test
    void shouldFindCommentByBookTitle(){
        given(bookDao.findByTitle(EXISTING_BOOK_TITLE)).willReturn(Optional.of(Book.builder().build()));
        given(commentDao.findByBookTitle(any())).willReturn(comments);
        assertEquals(comments, commentService.findByBookTitle(EXISTING_BOOK_TITLE));
    }

    @DisplayName("должен найти все комментарии")
    @Test
    void shouldFindAllComments(){
        given(commentDao.findAll()).willReturn(comments);
        assertEquals(comments, commentService.findAll());
    }

    @DisplayName("должен удалить комментарий по id")
    @Test
    void shouldDeleteCommentById(){
        given(commentDao.findById(EXISTING_COMMENT_ID)).willReturn(Optional.of(comments.get(0)));
        commentService.deleteById(EXISTING_COMMENT_ID);
        assertAll(
                () -> verify(commentDao, times(1)).deleteById(EXISTING_COMMENT_ID),
                () -> assertThrows(CommentNotFoundException.class,
                        () -> commentService.deleteById(NEW_COMMENT_ID))
        );
    }

    @DisplayName("должен добавлить коментарий")
    @Test
    void shouldInsertComment(){
        Comment comment = Comment.builder().text(NEW_COMMENT_TEXT).book(Book.builder().id(EXISTING_BOOK_ID).
                title(EXISTING_BOOK_TITLE).build()).build();
        given(commentDao.save(any())).willReturn(comment);
        given(bookDao.findByTitle(any())).willReturn(Optional.of(comment.getBook()));
        assertAll(
                () -> assertEquals(commentService.insert(CommentDto.toDto(comment), EXISTING_BOOK_TITLE).getText(), comment.getText()),
                () -> assertThrows(CommentAlreadyExistsException.class, () -> commentService.insert(
                        CommentDto.toDto(comments.get(0)), EXISTING_BOOK_TITLE))
        );
    }
}
