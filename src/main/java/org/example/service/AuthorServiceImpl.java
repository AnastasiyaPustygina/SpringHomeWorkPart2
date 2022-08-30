package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.AuthorDao;
import org.example.domain.Author;

import java.util.List;

@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{

    private final AuthorDao dao;

    @Override
    public Author findById(long id) {
        return dao.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return dao.findAll();
    }

    @Override
    public long insert(Author author) {
        return dao.insert(author);
    }

    @Override
    public void deleteById(long id) {
        dao.deleteById(id);
    }
}
