package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.GenreDao;
import org.example.domain.Genre;
import org.example.exception.AuthorNotFoundException;
import org.example.exception.GenreAlreadyExistsException;
import org.example.exception.GenreNotFoundException;
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
        try{
            dao.findByName(genre.getName());
            throw new GenreAlreadyExistsException("genre with name " + genre.getName() + " already exists");
        }catch (GenreNotFoundException e) {
            return dao.insert(genre);
        }
    }

    @Override
    public void deleteByName(String name) {
        try{
            dao.findByName(name);
            dao.deleteByName(name);
        }catch (GenreNotFoundException e) {
            throw new GenreNotFoundException("Genre with name " + name  + " was not found");
        }
    }


}
