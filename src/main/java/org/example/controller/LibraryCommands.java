package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.DemoService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
@ShellComponent
@RequiredArgsConstructor
public class LibraryCommands {
    private final DemoService demoService;

    @ShellMethod(value = "show all authors", key = {"saa", "show all authors"})
    public void showAllAuthors(){
        demoService.printAllAuthors();
    }

    @ShellMethod(value = "show all genres", key = {"sag", "show all genres"})
    public void showAllGenres(){
        demoService.printAllGenres();
    }

    @ShellMethod(value = "show all books", key = {"sab", "show all books"})
    public void showAllBooks(){
        demoService.printAllBooks();
    }

    @ShellMethod(value = "show author by name", key = {"sa", "show author by name"})
    public void showAuthorByName(){
        demoService.printAuthorsByName();
    }

    @ShellMethod(value = "show genre by name", key = {"sg", "show genre by name"})
    public void showGenreByName(){
        demoService.printGenresByName();
    }

    @ShellMethod(value = "show book by title", key = {"sb", "show book by title"})
    public void showBookByTitle(){
        demoService.printBooksByTitle();
    }

    @ShellMethod(value = "insert author", key = {"ia", "insert author"})
    public void insertAuthor(){
        demoService.insertAuthor();
    }

    @ShellMethod(value = "insert genre", key = {"ig", "insert genre"})
    public void insertGenre(){
        demoService.insertGenre();
    }

    @ShellMethod(value = "insert book", key = {"ib", "insert book"})
    public void insertBook(){
        demoService.insertBook();
    }

    @ShellMethod(value = "delete author by name", key = {"da", "delete author by name"})
    public void deleteAuthorByName(){
        demoService.deleteByAuthorName();
    }

    @ShellMethod(value = "delete genre by name", key = {"dg", "delete genre by name"})
    public void deleteGenreByName(){
        demoService.deleteByGenreName();
    }

    @ShellMethod(value = "delete book by name", key = {"db", "delete book by name"})
    public void deleteBookByName(){
        demoService.deleteByBookTitle();
    }

}
