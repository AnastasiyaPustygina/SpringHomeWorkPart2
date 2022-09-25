package org.example.service.demo;

import java.util.List;
import java.util.Scanner;
import org.example.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.example.service.BookService;
import org.example.service.CommentService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentDemoServiceImpl implements CommentDemoService{

    private static final String ENTER_COMMENT_ID = "Enter the comment id";
    private static final String ENTER_BOOK_TITLE = "Enter the book title";
    private static final String ENTER_COMMENT_TEXT = "Enter the new comment text";

    private final CommentService commentService;
    private final BookService bookService;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void printById() {
        System.out.println(ENTER_COMMENT_ID);
        System.out.println(commentService.findById(scanner.nextLong()));
    }

    @Override
    public void printAll() {
        commentService.findAll().forEach(System.out::println);
    }

    @Override
    public void insert() {
        System.out.println(ENTER_BOOK_TITLE);
        String bookTitle = scanner.nextLine();
        System.out.println(ENTER_COMMENT_TEXT);
        commentService.insert(Comment.builder().text(scanner.nextLine()).book(
                bookService.findByTitle(bookTitle)).build());
    }

    @Override
    public void updateTextById() {
        System.out.println(ENTER_COMMENT_ID);
        long id = scanner.nextLong();
        System.out.println(ENTER_COMMENT_TEXT);
        scanner.nextLine();
        String text = scanner.nextLine();
        commentService.updateTextById(id, text);
    }

    @Override
    public void deleteById() {
        System.out.println(ENTER_COMMENT_ID);
        commentService.deleteById(scanner.nextLong());
    }

    @Override
    public void printByBookTitle() {
        System.out.println(ENTER_BOOK_TITLE);
        List<Comment> comments = commentService.findByBookTitle(scanner.nextLine());
        if(comments.isEmpty()) System.out.println("This book has no comments");
        comments.forEach(System.out::println);
    }
}
