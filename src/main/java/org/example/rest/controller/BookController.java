package org.example.rest.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.Book;
import org.example.rest.dto.BookDto;
import org.example.rest.dto.CommentDto;
import org.example.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/book/")
    public List<BookDto> findAllBooks(){
        List<Book> books = bookService.findAll();
        return books.stream().map(BookDto::toDto).toList();
    }

    @GetMapping("/book/{title}")
    public BookDto findBookByTitle(@PathVariable String title){
        Book book = bookService.findByTitle(title);
        return BookDto.toDto(book);
    }

    @PostMapping("/book")
    public BookDto insertBook(@RequestBody BookDto bookDto){
        Book book = bookService.insert(BookDto.toDomainObject(bookDto, Collections.emptyList()));
        return BookDto.toDto(book);
    }

    @DeleteMapping("/book/{title}")
    public void deleteBookByTitle(@PathVariable String title){
        bookService.deleteByTitle(title);
    }
}
