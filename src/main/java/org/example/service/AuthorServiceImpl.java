package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.AuthorDao;
import org.example.domain.Author;
import org.example.exception.AuthorAlreadyExistsException;
import org.example.exception.AuthorNotFoundException;
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
    public Author insert(Author author) throws AuthorAlreadyExistsException {
        try{
            findByName(author.getName());
            throw new AuthorAlreadyExistsException(
                    "author with name " + author.getName() + " already exists");
        }catch (AuthorNotFoundException e){
            return dao.insert(author);
        }
    }

    @Override
    public void deleteByName(String name) throws AuthorNotFoundException{
        try {
            dao.findByName(name);
            dao.deleteByName(name);
        }catch (AuthorNotFoundException e){
            throw new AuthorNotFoundException("Author with name " + name  + " was not found");
        }
    }
}
