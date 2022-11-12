package org.example.dao;


import org.example.domain.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "author")
public interface AuthorDao extends JpaRepository<Author, Long> {

    @RestResource(path = "name", rel = "name")
    Optional<Author> findByName(String name);

    void deleteByName(String name);

}
