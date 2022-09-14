package org.example.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "text")
    private String text;

    @ManyToOne(targetEntity = Author.class, fetch = FetchType.LAZY)
    private Author author;

    @ManyToOne(targetEntity = Genre.class, fetch = FetchType.LAZY)
    private Genre genre;

    @Override
    public String toString() {
        return "id: " + id + "\n" +
                "title: " + title + " \n" +
                "text: " + text;
    }

}
