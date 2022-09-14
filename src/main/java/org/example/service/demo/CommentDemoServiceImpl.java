package org.example.service.demo;

import java.util.Scanner;
import org.example.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.example.service.CommentService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentDemoServiceImpl implements CommentDemoService{

    private static final String ENTER_COMMENT_ID = "Enter the comment id";
    private static final String ENTER_COMMENT_TEXT = "Enter the new comment text";
    private final CommentService commentService;
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
        System.out.println(ENTER_COMMENT_TEXT);
        System.out.println(commentService.insert(Comment.builder().text(scanner.nextLine()).build()));
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
}
