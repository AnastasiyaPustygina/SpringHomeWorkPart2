package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.demo.GenreDemoService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class GenreShellCommands {

    private final GenreDemoService demoService;

    @ShellMethod(value = "show all genres", key = {"sag", "show all genres"})
    public void showAll(){
        demoService.printAll();
    }

    @ShellMethod(value = "show genre by name", key = {"sg", "show genre by name"})
    public void showByName(){
        demoService.printByName();
    }

    @ShellMethod(value = "insert genre", key = {"ig", "insert genre"})
    public void insertGenre(){
        demoService.insert();
    }

    @ShellMethod(value = "delete genre by name", key = {"dg", "delete genre by name"})
    public void deleteGenreByName(){
        demoService.deleteByName();
    }

}
