package org.example.rest.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.Author;
import org.example.exception.AuthorAlreadyExistsException;
import org.example.exception.AuthorNotFoundException;
import org.example.rest.dto.AuthorDto;
import org.example.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController{
    private final AuthorService authorService;

    @GetMapping("/author/")
    public List<AuthorDto> getAllAuthors(){
        return authorService.findAll().stream().map(AuthorDto::toDto).toList();
    }

    @GetMapping("/author/{name}")
    public AuthorDto getAuthorByName(@PathVariable String name){
        return AuthorDto.toDto(authorService.findByName(name));
    }

    @PostMapping("/author")
    public AuthorDto insertAuthor(@RequestBody AuthorDto authorDto){
        Author author = authorService.insert(AuthorDto.toDomainObject(authorDto));
        return AuthorDto.toDto(author);
    }

    @DeleteMapping("/author/{name}")
    public void deleteAuthor(@PathVariable String name){
        authorService.deleteByName(name);
    }

    @ExceptionHandler({AuthorNotFoundException.class, AuthorAlreadyExistsException.class})
    public ResponseEntity<String> handlerAuthorException(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
