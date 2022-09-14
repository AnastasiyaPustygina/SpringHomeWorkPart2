package org.example.service.demo;

import lombok.RequiredArgsConstructor;
import org.example.domain.Author;
import org.example.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class AuthorDemoServiceImpl implements AuthorDemoService{

    public static final String ENTER_AUTHOR_NAME = "Enter the author's name";
    private final AuthorService authorService;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void insert() {
        System.out.println("Enter the author's name");
        authorService.insert(Author.builder().name(scanner.nextLine()).build());
    }

    @Override
    public void deleteByName() {
        System.out.println(ENTER_AUTHOR_NAME);
        authorService.deleteByName(scanner.nextLine());
    }

    @Override
    public void printByName() {
        System.out.println(ENTER_AUTHOR_NAME);
        System.out.println(authorService.findByName(scanner.nextLine()));
    }

    @Override
    public void printAll() {
        authorService.findAll().forEach(System.out::println);
    }

}
