package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.BookDao;
import org.example.dao.CommentDao;
import org.example.domain.Book;
import org.example.domain.Comment;
import org.example.exception.BookNotFoundException;
import org.example.exception.CommentAlreadyExistsException;
import org.example.exception.CommentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentDao dao;
    private final BookDao bookDao;

    @Override
    public Comment findById(long id) throws CommentNotFoundException{
        return dao.findById(id).orElseThrow(() -> new CommentNotFoundException(
                "comment with id " + id + " was not found"));
    }

    @Override
    public List<Comment> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional
    public Comment insert(Comment comment) throws CommentAlreadyExistsException,
            BookNotFoundException{
        if(comment.getId() <= 0){
            return dao.insert(comment);
        }
        throw new CommentAlreadyExistsException("comment with id " + comment.getId() +
                " already exists");
    }

    @Override
    @Transactional
    public void updateTextById(long id, String text) throws CommentNotFoundException{
        if(dao.findById(id).isEmpty()){
            throw new CommentNotFoundException("comment with id " + id + " was not found");
        }
        dao.updateTextById(id, text);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        if(dao.findById(id).isEmpty()){
            throw new CommentNotFoundException("comment with id " + id + " was not found");
        }
        dao.deleteById(id);
    }
}
