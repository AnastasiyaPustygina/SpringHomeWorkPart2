package org.example.dao;

import org.example.domain.Author;
import org.example.domain.Book;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class AuthorDaoImpl implements AuthorDao{

    private final NamedParameterJdbcOperations jdbc;
    private final BookDao bookDao;
    private final List<Author> authors;

    public AuthorDaoImpl(NamedParameterJdbcOperations jdbc, @Lazy BookDao bookDao) {
        this.bookDao = bookDao;
        this.jdbc = jdbc;
        authors = getAllAuthorsFromDB();
    }

    @Override
    public Author findById(long id) throws AuthorNotFoundException{
        return authors.stream().filter(a -> a.getId() == id).findAny().orElseThrow(
                () -> new AuthorNotFoundException("Author with id " + id  + " was not found"));
    }

    @Override
    public List<Author> findAll() {
        return authors;
    }

    @Override
    public long insert(Author author) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author_name", author.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into authors (author_name) values(:author_name)", params, keyHolder,
                new String[]{"id"});
        long authorId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        authors.add(Author.builder().id(authorId).name(author.getName()).build());
        return authorId;
    }

    @Override
    public void deleteById(long id) throws AuthorNotFoundException{
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        authors.stream().filter((a) -> a.getId() == id).findAny().ifPresentOrElse(
                authors::remove,
                new Runnable() {
                    @Override
                    public void run() {
                        throw new AuthorNotFoundException("Author with id " + id  + " was not found");
                    }
                });
        bookDao.deleteBooksByAuthorId(id);
        jdbc.update("delete from authors where id = :id", params);
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
