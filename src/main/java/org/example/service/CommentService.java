package org.example.service;

import org.example.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Comment findById(long id);

    List<Comment> findAll();

    List<Comment> findByBookTitle(String title);

    Comment insert(Comment comment);

    void updateTextById(long id, String text);

    void deleteById(long id);


}
