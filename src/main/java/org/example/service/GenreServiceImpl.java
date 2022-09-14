package org.example.service;

import org.example.dao.GenreDao;
import org.example.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.example.exception.GenreNotFoundException;
import org.example.exception.GenreAlreadyExistsException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService{

    private final GenreDao dao;

    @Override
    public Genre findByName(String name) {
        return dao.findByName(name).orElseThrow(() -> new GenreNotFoundException(
                "genre with name " + name + " was not found"));
    }

    @Override
    public List<Genre> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional
    public Genre insert(Genre genre) {
        if (genre.getId() <= 0){
            return dao.insert(genre);
        }
        throw new GenreAlreadyExistsException("genre with name " + genre.getName() +
                " already exists");
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        if(dao.findByName(name).isEmpty()){
            throw new GenreNotFoundException("genre with name " + name + " was not found");
        }
        dao.deleteByName(name);
    }


}
