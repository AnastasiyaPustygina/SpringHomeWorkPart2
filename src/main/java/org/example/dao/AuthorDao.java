package org.example.dao;


import org.example.domain.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorDao extends JpaRepository<Author, Long> {

    Optional<Author> findByName(String name);

    void deleteByName(String name);

}
