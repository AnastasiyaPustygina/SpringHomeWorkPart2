package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.demo.AuthorDemoService;
import org.example.service.demo.BookDemoService;
import org.example.service.demo.GenreDemoService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
@ShellComponent
@RequiredArgsConstructor
public class AuthorShellCommands {

    private final AuthorDemoService demoService;

    @ShellMethod(value = "show all authors", key = {"saa", "show all authors"})
    public void showAll(){
        demoService.printAll();
    }

    @ShellMethod(value = "show author by name", key = {"sa", "show author by name"})
    public void showByName(){
        demoService.printByName();
    }

    @ShellMethod(value = "insert author", key = {"ia", "insert author"})
    public void insert(){
        demoService.insert();
    }

    @ShellMethod(value = "delete author by name", key = {"da", "delete author by name"})
    public void deleteByName(){
        demoService.deleteByName();
    }

}
