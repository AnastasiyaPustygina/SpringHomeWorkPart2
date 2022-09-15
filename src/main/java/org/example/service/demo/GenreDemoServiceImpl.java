package org.example.service.demo;

import lombok.RequiredArgsConstructor;
import org.example.domain.Genre;
import org.example.service.GenreService;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class GenreDemoServiceImpl implements GenreDemoService{

    private static final String ENTER_GENRE_NAME = "Enter the genre name";
    private final GenreService genreService;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void insert() {
        System.out.println(ENTER_GENRE_NAME);
        genreService.insert(Genre.builder().name(scanner.nextLine()).build());
    }

    @Override
    public void printByName() {
        System.out.println(ENTER_GENRE_NAME);
        System.out.println(genreService.findByName(scanner.nextLine()));
    }

    @Override
    public void deleteByName() {
        System.out.println(ENTER_GENRE_NAME);
        genreService.deleteByName(scanner.nextLine());
    }

    @Override
    public void printAll() {
        genreService.findAll().forEach(System.out::println);
    }
}
