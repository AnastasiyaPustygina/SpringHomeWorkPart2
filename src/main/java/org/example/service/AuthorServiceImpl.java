package org.example.service;

import org.example.dao.AuthorDao;
import org.example.domain.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.example.exception.AuthorNotFoundException;
import org.example.exception.AuthorAlreadyExistsException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{

    private final AuthorDao dao;

    @Override
    public Author findByName(String name) throws AuthorNotFoundException {
        return dao.findByName(name).orElseThrow(() -> new AuthorNotFoundException(
                "author with name " + name + " was not found"));
    }

    @Override
    public List<Author> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional
    public Author insert(Author author) throws AuthorAlreadyExistsException {
        if(dao.findByName(author.getName()).isPresent()){
            throw new AuthorAlreadyExistsException("author with name " + author.getName() +
                    " already exists");
        }
        return dao.save(author);
    }

    @Override
    @Transactional
    public void deleteByName(String name) throws AuthorNotFoundException{
        if(dao.findByName(name).isEmpty()){
            throw new AuthorNotFoundException("Author with name " + name  + " was not found");
        }
        dao.deleteByName(name);
    }
}
