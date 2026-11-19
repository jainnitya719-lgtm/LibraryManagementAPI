package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {
    private final JdbcTemplate jdbc;

    public BookRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Book> mapper = (rs, rowNum) ->
            new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getInt("available_copies")
            );

    public int save(Book b) {
        String sql = "INSERT INTO books "
                + "(id, title, author, category, available_copies) "
                + "VALUES (?, ?, ?, ?, ?)";
        return jdbc.update(sql, b.getId(), b.getTitle(), b.getAuthor(),
                b.getCategory(), b.getAvailableCopies());
    }

    public List<Book> findAll() {
        return jdbc.query("SELECT * FROM books ORDER BY id", mapper);
    }

    public Optional<Book> findById(int id) {
        List<Book> result = jdbc.query(
                "SELECT * FROM books WHERE id = ?", mapper, id);
        return result.stream().findFirst();
    }

    public int update(int id, Book b) {
        String sql = "UPDATE books SET title = ?, author = ?, "
                + "category = ?, available_copies = ? WHERE id = ?";
        return jdbc.update(sql, b.getTitle(), b.getAuthor(),
                b.getCategory(), b.getAvailableCopies(), id);
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM books WHERE id = ?", id);
    }
}
