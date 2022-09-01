package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.AuthorDao;
import org.example.domain.Author;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{

    private final AuthorDao dao;

    @Override
    public Author findByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public List<Author> findAll() {
        return dao.findAll();
    }

    @Override
    public Author insert(Author author) {
        return dao.insert(author);
    }

    @Override
    public void deleteByName(String name) {
        dao.deleteByName(name);
    }
}
