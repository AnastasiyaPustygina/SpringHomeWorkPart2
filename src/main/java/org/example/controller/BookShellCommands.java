package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.demo.BookDemoService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class BookShellCommands {

    private final BookDemoService demoService;

    @ShellMethod(value = "show all books", key = {"sab", "show all books"})
    public void showAll(){
        demoService.printAll();
    }

    @ShellMethod(value = "show book by title", key = {"sb", "show book by title"})
    public void showByTitle(){
        demoService.printByTitle();
    }

    @ShellMethod(value = "insert book", key = {"ib", "insert book"})
    public void insertBook(){
        demoService.insert();
    }

    @ShellMethod(value = "delete book by title", key = {"db", "delete book by title"})
    public void deleteByTitle(){
        demoService.deleteByTitle();
    }

}
