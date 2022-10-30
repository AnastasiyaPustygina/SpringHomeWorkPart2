package org.example.rest.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.Genre;
import org.example.exception.GenreAlreadyExistsException;
import org.example.exception.GenreNotFoundException;
import org.example.rest.dto.GenreDto;
import org.example.service.GenreService;
import org.example.sequity.JpaUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController{
    private final GenreService genreService;
    private final JpaUserDetailsService service;

    @GetMapping("/genre/")
    public List<GenreDto> getAllGenres(){
        return genreService.findAll().stream().map(GenreDto::toDto).toList();
    }

    @GetMapping("/genre/{name}")
    public GenreDto getGenreByName(@PathVariable String name){
        return GenreDto.toDto(genreService.findByName(name));
    }

    @PostMapping("/genre")
    public GenreDto insertGenre(@RequestBody GenreDto genreDto){
        Genre genre = genreService.insert(GenreDto.toDomainObject(genreDto));
        return GenreDto.toDto(genre);
    }

    @DeleteMapping("/genre/{name}")
    public void deleteGenre(@PathVariable String name){
        genreService.deleteByName(name);
    }

    @ExceptionHandler({GenreNotFoundException.class, GenreAlreadyExistsException.class})
    public ResponseEntity<String> handlerGenreException(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}