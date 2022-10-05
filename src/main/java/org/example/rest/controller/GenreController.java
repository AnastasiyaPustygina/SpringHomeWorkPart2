package org.example.rest.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.Genre;
import org.example.rest.dto.GenreDto;
import org.example.service.GenreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController{
    private final GenreService genreService;

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

}