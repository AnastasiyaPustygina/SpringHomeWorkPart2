package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.GenreDao;
import org.example.domain.Genre;

import java.util.List;

@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService{

    private final GenreDao dao;

    @Override
    public Genre findById(long id) {
        return dao.findById(id);
    }

    @Override
    public List<Genre> findAll() {
        return dao.findAll();
    }

    @Override
    public long insert(Genre genre) {
        return dao.insert(genre);
    }

    @Override
    public void deleteById(long id) {
        dao.deleteById(id);
    }
}
