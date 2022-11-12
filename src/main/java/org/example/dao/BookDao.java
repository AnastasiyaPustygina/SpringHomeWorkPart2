package org.example.dao;

import java.util.List;
import java.util.Optional;

import org.example.domain.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(path = "book")
public interface BookDao extends JpaRepository<Book, Long> {

    @RestResource(path = "title", rel = "title")
    Optional<Book> findByTitle(String title);

    @Override
    @EntityGraph(attributePaths = {"author", "genre", "comments"})
    List<Book> findAll();

    void deleteByTitle(String title);

}
