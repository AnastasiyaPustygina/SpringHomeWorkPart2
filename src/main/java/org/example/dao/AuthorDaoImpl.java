package org.example.dao;

import org.example.domain.Author;
import org.example.exception.AuthorAlreadyExistsException;
import org.example.exception.AuthorNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class AuthorDaoImpl implements AuthorDao{

    private final NamedParameterJdbcOperations jdbc;
    private final BookDao bookDao;
    private List<Author> authors;

    public AuthorDaoImpl(NamedParameterJdbcOperations jdbc, @Lazy BookDao bookDao) {
        this.bookDao = bookDao;
        this.jdbc = jdbc;
        authors = getAllAuthorsFromDB();
    }

    @Override
    public Author findByName(String name){
        return authors.stream().filter(a -> a.getName().equals(name)).findAny().orElseThrow(
                () -> new AuthorNotFoundException("author with name " + name + " was not found"));
    }

    @Override
    public List<Author> findAll() {
        return authors;
    }

    @Override
    public Author insert(Author author) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author_name", author.getName());
        if(authors.stream().anyMatch(a -> a.getName().equals(author.getName())))
            throw new AuthorAlreadyExistsException("author with name " + author.getName() + " already exists");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into authors (author_name) values(:author_name)", params, keyHolder,
                new String[]{"id"});
        long authorId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        Author resultingAuthor = Author.builder().id(authorId).name(author.getName()).build();
        authors.add(resultingAuthor);
        return resultingAuthor;
    }

    @Override
    public void deleteByName(String name) throws AuthorNotFoundException{
        Map<String, Object> params = new HashMap<>();
        authors.stream().filter((a) -> a.getName().equals(name)).findAny().ifPresentOrElse(
                (a) -> {
                    authors.remove(a);
                    params.put("id", a.getId());
                },
                new Runnable() {
                    @Override
                    public void run() {
                        throw new AuthorNotFoundException("Author with name " + name  + " was not found");
                    }
                });
        bookDao.deleteBooksByAuthorId((Long) params.get("id"));
        jdbc.update("delete from authors where id = :id", params);
    }

    @Override
    public void setAuthors(List<Author> authors){
        this.authors = new ArrayList<>();
        this.authors.addAll(authors);
    }

    private List<Author> getAllAuthorsFromDB(){
        return jdbc.query("select * from authors", new AuthorMapper());
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("author_name");
            return Author.builder().id(id).name(name).build();
        }
    }
}
