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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Repository
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations jdbc;
    private List<Book> books;

    public BookDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        books = getAllBooksFromDB();
    }


    @Override
    public Book findByTitle(String title) throws BookNotFoundException{
        return books.stream().filter(b -> b.getTitle().equals(title)).findAny().orElseThrow(
                () -> new BookNotFoundException("book with title " + title + " was not found"));
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Book insert(Book book) {
        long authorId = book.getAuthor().getId();
        long genreId = book.getGenre().getId();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("text", book.getText());
        params.addValue("author_id", authorId);
        params.addValue("genre_id", genreId);
        jdbc.update("insert into books (title, text, author_id, genre_id) values " +
                "(:title, :text, :author_id, :genre_id)", params);
        long bookId = getBookIdFromDB(book.getTitle());
        Book resultingBook = Book.builder().id(bookId).title(book.getTitle()).text(book.getText())
                .author(Author.builder().id(authorId).name(book.getAuthor().getName()).build())
                .genre(Genre.builder().id(genreId).name(book.getGenre().getName()).build()).build();
        books.add(resultingBook);
        return resultingBook;
    }

    @Override
    public void deleteByTitle(String title){
        Map<String, Object> params = new HashMap<>();
        books.stream().filter((b) -> b.getTitle().equals(title)).findAny().ifPresent(
                (b) -> {
                    books.remove(b);
                    params.put("id", b.getId());
                });
        jdbc.update("delete from books where id = :id", params);
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
        List<Book> bookList = new ArrayList<>(books);
        bookList.stream().filter(b -> b.getGenre().getId() == genre_id).forEach(books::remove);
    }

    @Override
    public void setBooks(List<Book> books) {
        this.books = new ArrayList<>();
        this.books.addAll(books);
    }


    private List<Book> getAllBooksFromDB(){
        return jdbc.query("select books.id, books.title, books.text, books.author_id," +
                "books.genre_id, authors.name as author_name, genres.name as genre_name " +
                " from books inner join authors on books.author_id = authors.id " +
                "join genres on books.genre_id = genres.id", new BookMapper());
    }
    private long getBookIdFromDB(String title){
        RowMapper<Integer> rowMapper = (rs, rowNum) -> rs.getInt("id");
        return jdbc.query("select id from books where " +
                "title = '" + title + "'", rowMapper).get(0);
    }
    private static class BookMapper implements RowMapper<Book>{

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {

            long id = rs.getLong("id");
            String title = rs.getString("title");
            String text = rs.getString("text");
            Author author = Author.builder().id(rs.getLong("author_id")).
                    name(rs.getString("author_name")).build();
            Genre genre = Genre.builder().id(rs.getLong("genre_id")).
                    name(rs.getString("genre_name")).build();
            return Book.builder().id(id).title(title).text(text).author(author).genre(genre).build();
        }
    }
}
