package org.example.service.demo;

import lombok.RequiredArgsConstructor;
import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Genre;
import org.example.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class BookDemoServiceImpl implements BookDemoService{

    private static final String ENTER_GENRE_NAME = "Enter the genre name";
    public static final String ENTER_BOOK_TITLE = "Enter the book title";
    public static final String ENTER_AUTHOR_NAME = "Enter the author's name";
    private final Scanner scanner = new Scanner(System.in);
    private final BookService bookService;

    @Override
    public void insert() {
        System.out.println(ENTER_BOOK_TITLE);
        String title = scanner.nextLine();
        System.out.println("Enter the text of the book");
        String text = scanner.nextLine();
        System.out.println(ENTER_AUTHOR_NAME);
        String author_name = scanner.nextLine();
        System.out.println(ENTER_GENRE_NAME);
        String genre_name = scanner.nextLine();
        bookService.insert(Book.builder().title(title).text(text).author(Author.builder().name(author_name)
                .build()).genre(Genre.builder().name(genre_name).build()).comments(Collections.emptyList()).build());
    }

    @Override
    public void deleteByTitle() {
        System.out.println(ENTER_BOOK_TITLE);
        bookService.deleteByTitle(scanner.nextLine());
    }

    @Override
    public void printByTitle() {
        System.out.println(ENTER_BOOK_TITLE);
        System.out.println(bookService.findByTitle(scanner.nextLine()));
    }

    @Override
    public void printAll() {
        bookService.findAll().forEach(System.out::println);
    }
}
