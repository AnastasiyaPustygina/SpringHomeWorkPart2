package org.example.rest.dto;

import lombok.Builder;
import lombok.Data;
import org.example.domain.Author;

@Data
@Builder
public class AuthorDto {

    private final long id;

    private final String name;

    public static AuthorDto toDto(Author author){
        return AuthorDto.builder().id(author.getId()).name(author.getName()).build();
    }

    public static Author toDomainObject(AuthorDto authorDto){
        return Author.builder().id(authorDto.getId()).name(authorDto.getName()).build();
    }

}
