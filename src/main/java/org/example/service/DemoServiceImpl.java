package org.example.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DemoServiceImpl implements DemoService{

    private final AuthorService authorService;
    private final BookService bookService;
    private final GenreService genreService;

    @Override
    public void demo() {

    }
    public void getAuthorById(){
        System.out.println("Введите id автора");
    }
}
