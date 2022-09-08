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
    private List<Genre> genres;

    public GenreDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        this.genres = getGenresFromDB();
    }


    @Override
    public Genre findByName(String name) throws GenreNotFoundException{
        return genres.stream().filter(g -> g.getName().equals(name)).findAny().orElseThrow( () ->
                new GenreNotFoundException("genre with name " + name + " was not found"));
    }

    @Override
    public List<Genre> findAll() {
        return genres;
    }

    @Override
    public Genre insert(Genre genre) {
        String name = genre.getName();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        jdbc.update("insert into genres (name) values (:name)", params);
        Genre resultingGenre = Genre.builder().id(getGenreIdFromDB(name)).name(name).build();
        genres.add(resultingGenre);
        return resultingGenre;
    }

    @Override
    public void deleteByName(String name){
        Map<String, Object> params = new HashMap<>();
        genres.stream().filter((g) -> g.getName().equals(name)).findAny().ifPresent(
                (a) -> {
                    genres.remove(a);
                    params.put("id", a.getId());
                });
        jdbc.update("delete from genres where id = :id", params);
    }

    @Override
    public void setGenres(List<Genre> genres) {
        this.genres = new ArrayList<>();
        this.genres.addAll(genres);
    }
    private long getGenreIdFromDB(String name){
        RowMapper<Integer> rowMapper = (rs, rowNum) -> rs.getInt("id");
        return jdbc.query("select id from genres where " +
                "name = '" + name + "'", rowMapper).get(0);
    }

    private List<Genre> getGenresFromDB(){
        return jdbc.query("select * from genres", new GenreMapper());
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return Genre.builder().id(id).name(name).build();
        }
    }
}
