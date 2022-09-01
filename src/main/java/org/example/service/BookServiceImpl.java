package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.BookDao;
import org.example.domain.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao dao;


    @Override
    public Book findByTitle(String title) {
        return dao.findByTitle(title);
    }

    @Override
    public List<Book> findAll() {
        return dao.findAll();
    }

    @Override
    public Book insert(Book book) {
        return dao.insert(book);
    }

    @Override
    public void deleteByTitle(String title) {
        dao.deleteByTitle(title);
    }
}
