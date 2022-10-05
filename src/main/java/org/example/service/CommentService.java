package org.example.service;

import org.example.domain.Comment;
import org.example.rest.dto.CommentDto;

import java.util.List;

public interface CommentService {

    Comment findById(long id);

    List<Comment> findAll();

    List<Comment> findByBookTitle(String title);

    Comment insert(CommentDto commentDto, String bookTitle);

    void updateTextById(long id, String text);

    void deleteById(long id);


}
