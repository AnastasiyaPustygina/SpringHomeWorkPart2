package org.example.dao;

import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Genre;
import org.example.exception.AuthorNotFoundException;
import org.example.exception.BookNotFoundException;
import org.example.exception.GenreNotFoundException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.security.auth.login.AccountNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Repository
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations jdbc;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final List<Book> books;

    public BookDaoImpl(NamedParameterJdbcOperations jdbc, AuthorDao authorDao, GenreDao genreDao) {
        this.jdbc = jdbc;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        books = getAllBooksFromDB();
    }

    @Override
    public Book findById(long id) throws BookNotFoundException {
        return books.stream().filter((b) -> b.getId() == id).findAny().orElseThrow(
                () -> new BookNotFoundException("Book with id " + id + " was not found"));
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public long insert(Book book) {
        long authorId = insertAuthorIfItDoesntExist(book.getAuthor());
        long genreId = insertGenreIfItDoesntExist(book.getGenre());
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("book_text", book.getText());
        params.addValue("author_id", authorId);
        params.addValue("genre_id", genreId);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into books (title, book_text, author_id, genre_id) values " +
                "(:title, :book_text, :author_id, :genre_id)", params, keyHolder, new String[]{"id"});
        long bookId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        books.add(Book.builder().id(bookId).title(book.getTitle()).text(book.getText())
                .author(Author.builder().id(authorId).name(book.getAuthor().getName()).build())
                .genre(Genre.builder().id(genreId).name(book.getGenre().getName()).build()).build());
        return bookId;
    }

    @Override
    public void deleteById(long id) throws BookNotFoundException{
        books.stream().filter((b) -> b.getId() == id).findAny().ifPresentOrElse(
                books::remove,
                new Runnable() {
                    @Override
                    public void run() {
                        throw new BookNotFoundException("Book with id " + id + " was not found");
                    }
                });
        jdbc.update("delete from books where id = :id", Map.of("id", id));
    }

    @Override
    public void deleteBooksByAuthorId(long author_id){
        jdbc.update("delete from books where author_id = :author_id", Map.of("author_id", author_id));
        List<Book> bookList = new ArrayList<>();
        bookList.addAll(books);
        bookList.stream().filter(b -> b.getAuthor().getId() == author_id).forEach(books::remove);
    }

    @Override
    public void deleteBooksByGenreId(long genre_id){
        jdbc.update("delete from books where genre_id = :genre_id", Map.of("genre_id", genre_id));
        List<Book> bookList = new ArrayList<>();
        bookList.addAll(books);
        bookList.stream().filter(b -> b.getGenre().getId() == genre_id).forEach(books::remove);
    }

    private List<Book> getAllBooksFromDB(){
        return jdbc.query("select * from books inner join authors on books.author_id = authors.id " +
                "join genres on books.genre_id = genres.id", new BookMapper());
    }

    private long insertAuthorIfItDoesntExist(Author author){
        try{
            authorDao.findById(author.getId());
            return author.getId();
        }catch (AuthorNotFoundException e){
            return authorDao.insert(author);
        }
    }
    private long insertGenreIfItDoesntExist(Genre genre){
        try{
            genreDao.findById(genre.getId());
            return genre.getId();
        }catch (GenreNotFoundException e){
            return genreDao.insert(genre);
        }
    }
    private static class BookMapper implements RowMapper<Book>{

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {

            long id = rs.getLong("id");
            String title = rs.getString("title");
            String text = rs.getString("book_text");
            Author author = Author.builder().id(rs.getLong("author_id")).
                    name(rs.getString("author_name")).build();
            Genre genre = Genre.builder().id(rs.getLong("genre_id")).
                    name(rs.getString("genre_name")).build();
            return Book.builder().id(id).title(title).text(text).author(author).genre(genre).build();
        }
    }
}
