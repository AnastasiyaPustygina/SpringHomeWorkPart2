package org.example.dao;

import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Comment;
import org.example.domain.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Класс CommentDao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentDaoTest {

    private static final String EXISTING_BOOK_TITLE = "Lovers of death";
    private static final long EXISTING_BOOK_ID = 1;
    private static final String EXISTING_BOOK_TEXT = "text of the book with name <<Lovers of death>>";
    private static final long EXISTING_AUTHOR_ID = 1;
    private static final String EXISTING_AUTHOR_NAME = "Boris Akunin";
    private static final long EXISTING_GENRE_ID = 1;
    private static final String EXISTING_GENRE_NAME = "detective";
    private static final long EXISTING_COMMENT_ID = 1;
    private static final String EXISTING_COMMENT_TEXT = "Content of first comment";
    private static final long SECOND_COMMENT_ID = 2;
    private static final String SECOND_COMMENT_TEXT = "Content of second comment";
    private static final String NEW_COMMENT_TEXT = "Content of new comment";
    private static final long COUNT_OF_COMMENT = 3;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("должен найти комментарий по id")
    void shouldFindCommentId(){
        assertThat(commentDao.findById(EXISTING_COMMENT_ID)).isPresent().get()
                .hasFieldOrPropertyWithValue("id", EXISTING_COMMENT_ID);
    }

    @Test
    @DisplayName("должен найти все комментрии")
    void shouldFindAllComments(){
        assertEquals(commentDao.findAll().size(), COUNT_OF_COMMENT);
    }

    @Test
    @DisplayName("должен добавить комментрий")
    void shouldInsetComment(){
        long sizeBefore = commentDao.findAll().size();
        Comment comment = Comment.builder().text(NEW_COMMENT_TEXT).book(Book.builder().id(EXISTING_BOOK_ID)
                .title(EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT).author(Author.builder()
                .id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build()).genre(Genre.builder()
                .id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build()).comments(Collections.emptyList()).build()).build();
        assertAll(
            () -> assertEquals(NEW_COMMENT_TEXT, commentDao.save(comment).getText()),
            () -> assertEquals(sizeBefore, commentDao.findAll().size() - 1)
        );
    }

    @Test
    @DisplayName("должен изменить текст комментрия по id")
    void shouldUpdateTextById(){
        assert(commentDao.findById(EXISTING_COMMENT_ID).isPresent());
        Comment comment = commentDao.findById(EXISTING_COMMENT_ID).get();
        assertNotEquals(NEW_COMMENT_TEXT, comment.getText());
        entityManager.detach(comment);
        commentDao.updateTextById(EXISTING_COMMENT_ID, NEW_COMMENT_TEXT);
        assertEquals(NEW_COMMENT_TEXT, commentDao.findById(EXISTING_COMMENT_ID).get().getText());

    }

    @Test
    @DisplayName("должен найти комменарии по названию книги")
    void shouldFindCommentsByBookTitle(){
        Comment comment = Comment.builder().id(EXISTING_COMMENT_ID).text(EXISTING_COMMENT_TEXT).book(Book.builder().id(EXISTING_BOOK_ID)
                .title(EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT).author(Author.builder()
                        .id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build()).genre(Genre.builder()
                        .id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build()).comments(Collections.emptyList()).build()).build();
        Comment comment2 = Comment.builder().id(SECOND_COMMENT_ID).text(SECOND_COMMENT_TEXT).book(Book.builder().id(EXISTING_BOOK_ID)
                .title(EXISTING_BOOK_TITLE).text(EXISTING_BOOK_TEXT).author(Author.builder()
                        .id(EXISTING_AUTHOR_ID).name(EXISTING_AUTHOR_NAME).build()).genre(Genre.builder()
                        .id(EXISTING_GENRE_ID).name(EXISTING_GENRE_NAME).build()).comments(Collections.emptyList()).build()).build();
        assertThat(List.of(comment, comment2)).usingRecursiveComparison().comparingOnlyFields("id")
                .isEqualTo(commentDao.findByBookTitle(EXISTING_BOOK_TITLE));
    }
}
