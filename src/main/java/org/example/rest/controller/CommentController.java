package org.example.rest.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.Comment;
import org.example.exception.CommentAlreadyExistsException;
import org.example.exception.CommentNotFoundException;
import org.example.rest.dto.CommentDto;
import org.example.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comment/")
    public List<CommentDto> findAllComments(){
        return commentService.findAll().stream().map(CommentDto::toDto).toList();
    }

    @GetMapping("/comment/{id}")
    public CommentDto findCommentById(@PathVariable long id){
        return CommentDto.toDto(commentService.findById(id));
    }

    @GetMapping("/comment/book/{bookTitle}")
    public List<CommentDto> findByBookTitle(@PathVariable String bookTitle){
        return commentService.findByBookTitle(bookTitle).stream().map(CommentDto::toDto).toList();
    }

    @PostMapping("/comment")
    public CommentDto insertComment(@RequestBody CommentDto commentDto, @RequestParam("bookTitle") String bookTitle){
        Comment comment = commentService.insert(commentDto, bookTitle);
        return CommentDto.toDto(comment);
    }

    @PatchMapping("/comment/{id}/text")
    public void updateComment(@PathVariable long id, @RequestParam("text") String text){
        commentService.updateTextById(id, text);
    }

    @DeleteMapping("/comment/{id}")
    public void deleteComment(@PathVariable long id){
        commentService.deleteById(id);
    }

    @ExceptionHandler({CommentNotFoundException.class, CommentAlreadyExistsException.class})
    public ResponseEntity<String> handlerCommentException(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
