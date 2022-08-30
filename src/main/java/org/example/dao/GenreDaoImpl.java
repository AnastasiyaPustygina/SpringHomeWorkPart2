package org.example.dao;

import org.example.domain.Genre;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class GenreDaoImpl implements GenreDao {

    private final NamedParameterJdbcOperations jdbc;
    private final BookDao bookDao;
    private final List<Genre> genres;

    public GenreDaoImpl(NamedParameterJdbcOperations jdbc, @Lazy BookDao bookDao) {
        this.bookDao = bookDao;
        this.jdbc = jdbc;
        this.genres = getGenresFromDB();
    }

    @Override
    public Genre findById(long id) throws GenreNotFoundException{
        return genres.stream().filter(g -> g.getId() == id).findAny().orElseThrow(
                () -> new GenreNotFoundException("Genre with id " + id + " was not found"));
    }

    @Override
    public List<Genre> findAll() {
        return genres;
    }

    @Override
    public long insert(Genre genre) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genre_name", genre.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into genres (genre_name) values (:genre_name)", params, keyHolder, new String[]{"id"});
        long genreId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        genres.add(Genre.builder().id(genreId).name(genre.getName()).build());
        return genreId;
    }

    @Override
    public void deleteById(long id) throws GenreNotFoundException {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        genres.stream().filter((g) -> g.getId() == id).findAny().ifPresentOrElse(
                genres::remove, new Runnable() {
                    @Override
                    public void run() {
                        throw new GenreNotFoundException(
                                "Genre with id " + id  + " was not found");
                    }
                });
        bookDao.deleteBooksByGenreId(id);
        jdbc.update("delete from genres where id = :id", params);
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
