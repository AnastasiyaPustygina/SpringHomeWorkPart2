package org.example.rest.dto;

import lombok.Builder;
import lombok.Data;
import org.example.domain.Genre;
@Data
@Builder
public class GenreDto{

    private final long id;

    private final String name;
    public static GenreDto toDto(Genre genre){
        return GenreDto.builder().id(genre.getId()).name(genre.getName()).build();
    }
    public static Genre toDomainObject(GenreDto genreDto){
        return Genre.builder().id(genreDto.getId()).name(genreDto.getName()).build();
    }

}