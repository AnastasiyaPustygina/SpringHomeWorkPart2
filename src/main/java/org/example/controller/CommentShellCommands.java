package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.demo.CommentDemoService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class CommentShellCommands {

    private final CommentDemoService demoService;

    @ShellMethod(value = "show all comments", key = {"sac", "show all comments"})
    public void showAll(){
        demoService.printAll();
    }

    @ShellMethod(value = "show comment by id", key = {"sc", "show comment by id"})
    public void showById(){
        demoService.printById();
    }

    @ShellMethod(value = "show comments by book title", key = {"scb", "show comment by book title"})
    public void showByBookTitle(){
        demoService.printByBookTitle();
    }

    @ShellMethod(value = "update comment text by id", key = {"uc", "update comment text by id"})
    public void updateTextById(){
        demoService.updateTextById();
    }

    @ShellMethod(value = "insert comment", key = {"ic", "insert comment"})
    public void insert(){
        demoService.insert();
    }

    @ShellMethod(value = "delete comment by id", key = {"dc", "delete comment by id"})
    public void deleteByName(){
        demoService.deleteById();
    }

}
