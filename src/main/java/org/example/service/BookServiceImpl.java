package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.BookDao;
import org.example.domain.Book;

import java.util.List;

@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao dao;

    @Override
    public Book findById(long id) {
        return dao.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return dao.findAll();
    }

    @Override
    public long insert(Book book) {
        return dao.insert(book);
    }

    @Override
    public void deleteById(long id) {
        dao.deleteById(id);
    }
}
