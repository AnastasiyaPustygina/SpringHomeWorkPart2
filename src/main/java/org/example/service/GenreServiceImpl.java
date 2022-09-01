package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.GenreDao;
import org.example.domain.Genre;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService{

    private final GenreDao dao;

    @Override
    public Genre findByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public List<Genre> findAll() {
        return dao.findAll();
    }

    @Override
    public Genre insert(Genre genre) {
        return dao.insert(genre);
    }

    @Override
    public void deleteByName(String name) {
        dao.deleteByName(name);
    }


}
