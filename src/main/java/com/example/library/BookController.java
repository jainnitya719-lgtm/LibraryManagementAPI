package com.example.library;

import java.util.List;
import java.util.Optional;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository repo;

    public BookController(BookRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        if (repo.existsById(book.getId())) {
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.status(201).body(repo.save(book));
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Integer id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Integer id,
            @RequestBody Book updated) {

        Optional<Book> result = repo.findById(id);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        Book book = result.get();
        book.setTitle(updated.getTitle());
        book.setAuthor(updated.getAuthor());
        book.setCategory(updated.getCategory());
        book.setAvailableCopies(updated.getAvailableCopies());

        return ResponseEntity.ok(repo.save(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Integer id) {

        if (!repo.existsById(id))
            return ResponseEntity.status(404).body("Book not found");

        repo.deleteById(id);
        return ResponseEntity.ok("Book deleted successfully");
    }
}
