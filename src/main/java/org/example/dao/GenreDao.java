package org.example.dao;

import org.example.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenreDao extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);

    void deleteByName(String name);

}
