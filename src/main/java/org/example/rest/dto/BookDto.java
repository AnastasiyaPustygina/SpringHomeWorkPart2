package org.example.rest.dto;

import lombok.Builder;
import lombok.Data;
import org.example.domain.Book;
import org.example.domain.Comment;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class BookDto{

    private final long id;

    private final String title;

    private final String text;

    private final AuthorDto authorDto;

    private final GenreDto genreDto;

    private final List<CommentDto> comments;

    public static BookDto toDto(Book book){
        List<Comment> comments = book.getComments();
        List<CommentDto> commentDtoList = comments == null ? Collections.emptyList() : comments.stream()
                .map(CommentDto::toDto).toList();
        return BookDto.builder().id(book.getId()).title(book.getTitle()).text(book.getText()).authorDto(
                AuthorDto.toDto(book.getAuthor())).genreDto(GenreDto.toDto(book.getGenre())).comments(commentDtoList).build();
    }
    public static Book toDomainObject(BookDto bookDto, List<Comment> comments){
        return Book.builder().id(bookDto.getId()).title(bookDto.getTitle()).text(bookDto.getText()).
                genre(GenreDto.toDomainObject(bookDto.getGenreDto())).author(AuthorDto.toDomainObject(
                        bookDto.getAuthorDto())).comments(comments).build();
    }

}