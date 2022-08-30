package org.example.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Book {
    long id;
    String title;
    String text;
    Author author;
    Genre genre;

}
