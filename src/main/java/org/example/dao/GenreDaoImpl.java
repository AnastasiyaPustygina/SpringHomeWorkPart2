package org.example.dao;

import org.example.domain.Genre;
import org.example.exception.GenreAlreadyExistsException;
import org.example.exception.GenreNotFoundException;
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
public class GenreDaoImpl implements GenreDao {

    private final NamedParameterJdbcOperations jdbc;
    private final BookDao bookDao;
    private List<Genre> genres;

    public GenreDaoImpl(NamedParameterJdbcOperations jdbc, @Lazy BookDao bookDao) {
        this.bookDao = bookDao;
        this.jdbc = jdbc;
        this.genres = getGenresFromDB();
    }


    @Override
    public Genre findByName(String name) {
        return genres.stream().filter(g -> g.getName().equals(name)).findAny().orElseThrow( () ->
                new GenreNotFoundException("genre with name " + name + " was not found"));
    }

    @Override
    public List<Genre> findAll() {
        return genres;
    }

    @Override
    public Genre insert(Genre genre) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genre_name", genre.getName());
        if(genres.stream().anyMatch(g -> g.getName().equals(genre.getName())))
            throw new GenreAlreadyExistsException("genre with name " + genre.getName() + "already exists");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into genres (genre_name) values (:genre_name)", params, keyHolder, new String[]{"id"});
        long genreId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        Genre resulting_genre = Genre.builder().id(genreId).name(genre.getName()).build();
        genres.add(resulting_genre);
        return resulting_genre;
    }

    @Override
    public void deleteByName(String name) throws GenreNotFoundException {
        Map<String, Object> params = new HashMap<>();
        genres.stream().filter((g) -> g.getName().equals(name)).findAny().ifPresentOrElse(
                (g) -> {
                    genres.remove(g);
                    params.put("id", g.getId());
                }, new Runnable() {
                    @Override
                    public void run() {
                        throw new GenreNotFoundException(
                                "Genre with name " + name  + " was not found");
                    }
                });
        bookDao.deleteBooksByGenreId((Long) params.get("id"));
        jdbc.update("delete from genres where id = :id", params);
    }

    @Override
    public void setGenres(List<Genre> genres) {
        this.genres = new ArrayList<>();
        this.genres.addAll(genres);
    }


    private List<Genre> getGenresFromDB(){
        return jdbc.query("select * from genres", new GenreMapper());
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("genre_name");
            return Genre.builder().id(id).name(name).build();
        }
    }
}
