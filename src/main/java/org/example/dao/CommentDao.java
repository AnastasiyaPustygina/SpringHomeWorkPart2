package org.example.dao;

import org.example.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDao {

    Optional<Comment> findById(long id);

    List<Comment> findAll();

    Comment insert(Comment comment);

    void updateTextById(long id, String text);

    void deleteById(long id);

}
