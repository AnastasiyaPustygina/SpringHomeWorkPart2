package org.example.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

//    было бы неплохо привязать коммент к написавшему его пользователю
//    @JoinColumn(name = "user_id")
//    @Getter
//    @Setter
//    Если рассматривать реальные комменты, то подгрузка скорее всего должна быть в
//    любом случае, т.к. только текст комментария без указания на написавшего мы
//    никогда не увидим (FetchType.EAGER)
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    User user;

    @Override
    public String toString() {
        return "id: " + id + "\n" + "text: " + text;
    }

}
