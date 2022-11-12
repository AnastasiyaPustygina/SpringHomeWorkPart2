package org.example.dao;

import org.example.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "genre")
public interface GenreDao extends JpaRepository<Genre, Long> {

    @RestResource(path = "name", rel = "name")
    Optional<Genre> findByName(String name);

    void deleteByName(String name);

}
