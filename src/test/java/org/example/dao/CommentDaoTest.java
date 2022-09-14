package org.example.dao;

import liquibase.pro.packaged.c;
import org.example.domain.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(CommentDaoImpl.class)
@DisplayName("Класс CommentDao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentDaoTest {

    private static final long EXISTING_COMMENT_ID = 1;
    private static final String EXISTING_COMMENT_TEXT = "Content of first comment";
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
        Comment comment = Comment.builder().text(NEW_COMMENT_TEXT).build();
        assertAll(
            () -> assertEquals(NEW_COMMENT_TEXT, commentDao.insert(comment).getText()),
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


}
