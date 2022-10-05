package org.example.rest.dto;

import lombok.Builder;
import lombok.Data;
import org.example.domain.Book;
import org.example.domain.Comment;
@Data
@Builder
public class CommentDto{

    private final long id;

    private final String text;

    public static CommentDto toDto(Comment comment){
        return CommentDto.builder().id(comment.getId()).text(comment.getText()).build();
    }
    public static Comment toDomainObject(CommentDto commentDto, Book book){
        return Comment.builder().id(commentDto.getId()).text(commentDto.getText())
                .book(book).build();
    }

}