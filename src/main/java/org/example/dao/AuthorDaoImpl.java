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
    private List<Author> authors;

    public AuthorDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        authors = getAllAuthorsFromDB();
    }

    @Override
    public Author findByName(String name) throws AuthorNotFoundException{
        return authors.stream().filter(a -> a.getName().equals(name)).findAny().orElseThrow(
                () -> new AuthorNotFoundException("author with name " + name + " was not found"));
    }

    @Override
    public List<Author> findAll() {
        return authors;
    }

    @Override
    public Author insert(Author author) {
        String name = author.getName();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        jdbc.update("insert into authors (name) values(:name)", params);
        Author resultingAuthor = Author.builder().id(getAuthorIdFromDB(name))
                .name(name).build();
        authors.add(resultingAuthor);
        return resultingAuthor;
    }

    @Override
    public void deleteByName(String name){
        Map<String, Object> params = new HashMap<>();
        authors.stream().filter((a) -> a.getName().equals(name)).findAny().ifPresent(
                (a) -> {
                    authors.remove(a);
                    params.put("id", a.getId());
                });
        jdbc.update("delete from authors where id = :id", params);
    }

    @Override
    public void setAuthors(List<Author> authors){
        this.authors = new ArrayList<>();
        this.authors.addAll(authors);
    }
    private long getAuthorIdFromDB(String name){
        RowMapper<Integer> rowMapper = (rs, rowNum) -> rs.getInt("id");
        return jdbc.query("select id from authors where " +
                "name = '" + name + "'", rowMapper).get(0);
    }

    private List<Author> getAllAuthorsFromDB(){
        return jdbc.query("select * from authors", new AuthorMapper());
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return Author.builder().id(id).name(name).build();
        }
    }
}
