package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.BookDao;
import org.example.dao.CommentDao;
import org.example.domain.Comment;
import org.example.exception.BookNotFoundException;
import org.example.exception.CommentAlreadyExistsException;
import org.example.exception.CommentNotFoundException;
import org.example.rest.dto.CommentDto;
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
    public List<Comment> findByBookTitle(String title) throws BookNotFoundException{
        bookDao.findByTitle(title);
        return dao.findByBookTitle(title);
    }

    @Override
    @Transactional
    public Comment insert(CommentDto commentDto, String bookTitle) throws CommentAlreadyExistsException,
            BookNotFoundException{
        Comment comment = Comment.builder().id(commentDto.getId()).text(commentDto.getText()).book(
                bookDao.findByTitle(bookTitle).orElseThrow(() -> new BookNotFoundException("book with title " + bookTitle + " was not found"))).build();
        if(commentDto.getId() <= 0){
            return dao.save(comment);
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
    public void deleteById(long id) throws CommentNotFoundException{
        if(dao.findById(id).isEmpty()){
            throw new CommentNotFoundException("comment with id " + id + " was not found");
        }
        dao.deleteById(id);
    }
}
