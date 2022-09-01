package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Genre;
import org.example.exception.AuthorNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@RequiredArgsConstructor
@Service
public class DemoServiceImpl implements DemoService{

    private static final String ENTER_GENRE_NAME = "Enter the generes's name";
    public static final String ENTER_BOOK_TITLE = "Enter the book's title";
    public static final String ENTER_AUTHOR_NAME = "Enter the author's name";
    private final AuthorService authorService;
    private final BookService bookService;
    private final GenreService genreService;
    private final Scanner scanner = new Scanner(System.in);


    @Override
    public void demo() {
        System.out.println("initial values: ");
        printAllAuthors();
        printAllGenres();
        printAllBooks();
        System.out.println("insert book");
        insertBook();
        System.out.println("values after adding new book with new author and genre");
        printAllAuthors();
        printAllGenres();
        printAllBooks();
        System.out.println("delete author");
        deleteByAuthorName();
        System.out.println("values after deleting author");
        printAllAuthors();
        printAllGenres();
        printAllBooks();
        System.out.println("find genre by name");
        printGenresByName();
    }

    @Override
    public void insertAuthor() {
        System.out.println("Enter the author's name");
        authorService.insert(Author.builder().name(scanner.nextLine()).build());
    }

    @Override
    public void insertBook() {
        System.out.println(ENTER_BOOK_TITLE);
        String title = scanner.nextLine();
        System.out.println("Enter the book's text");
        String text = scanner.nextLine();
        System.out.println(ENTER_AUTHOR_NAME);
        String author_name = scanner.nextLine();
        System.out.println(ENTER_GENRE_NAME);
        String genre_name = scanner.nextLine();
        bookService.insert(Book.builder().title(title).text(text).author(Author.builder().name(author_name)
                .build()).genre(Genre.builder().name(genre_name).build()).build());
    }

    @Override
    public void insertGenre() {
        System.out.println(ENTER_GENRE_NAME);
        genreService.insert(Genre.builder().name(scanner.nextLine()).build());
    }

    @Override
    public void deleteByAuthorName() {
        System.out.println(ENTER_AUTHOR_NAME);
        authorService.deleteByName(scanner.nextLine());
    }

    @Override
    public void deleteByGenreName() {
        System.out.println(ENTER_GENRE_NAME);
        genreService.deleteByName(scanner.nextLine());
    }

    @Override
    public void deleteByBookTitle() {
        System.out.println(ENTER_BOOK_TITLE);
        bookService.deleteByTitle(scanner.nextLine());
    }


    @Override
    public void printAuthorsByName() {
        System.out.println(ENTER_AUTHOR_NAME);
        System.out.println(authorService.findByName(scanner.nextLine()));
    }

    @Override
    public void printBooksByTitle() {
        System.out.println(ENTER_BOOK_TITLE);
        System.out.println(bookService.findByTitle(scanner.nextLine()));
    }

    @Override
    public void printGenresByName() {
        System.out.println(ENTER_GENRE_NAME);
        System.out.println(genreService.findByName(scanner.nextLine()));
    }

    @Override
    public void printAllBooks() {
        System.out.println(bookService.findAll());
    }

    @Override
    public void printAllAuthors() {
        System.out.println(authorService.findAll());
    }

    @Override
    public void printAllGenres() {
        System.out.println(genreService.findAll());
    }

}
