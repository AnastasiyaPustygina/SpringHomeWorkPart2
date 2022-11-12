package org.example.dao;

import java.util.List;

import org.example.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


@RepositoryRestResource(path = "comment")
public interface CommentDao extends JpaRepository<Comment, Long> {


    @Modifying
    @Query(value = "update Comment c set c.text = :text where c.id = :id")
    void updateTextById(@Param("id") long id, @Param("text") String text);

    @RestResource(path = "book/title", rel = "bookTitle")
    List<Comment> findByBookTitle(String title);
}
