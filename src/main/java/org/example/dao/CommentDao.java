package org.example.dao;

import java.util.List;
import org.example.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

public interface CommentDao extends JpaRepository<Comment, Long> {

    @Modifying
    @Query(value = "update Comment c set c.text = :text where c.id = :id")
    void updateTextById(@Param("id") long id, @Param("text") String text);

    List<Comment> findByBookTitle(String title);
}
