package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    // Add a book
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        if (book.getId() == null || book.getTitle() == null
                || book.getAuthor() == null || book.getCategory() == null
                || book.getAvailableCopies() == null || book.getAvailableCopies() < 0) {
            return ResponseEntity.badRequest()
                    .body("All fields are required; copies cannot be negative.");
        }
        if (repository.findById(book.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A book with this ID already exists.");
        }
        repository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    // Retrieve all books
    @GetMapping
    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    // Retrieve one book by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable int id) {
        return repository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Book not found with ID: " + id));
    }

    // Update book details
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable int id,
                                        @RequestBody Book book) {
        if (book.getTitle() == null || book.getAuthor() == null
                || book.getCategory() == null
                || book.getAvailableCopies() == null
                || book.getAvailableCopies() < 0) {
            return ResponseEntity.badRequest()
                    .body("Valid title, author, category and copies are required.");
        }
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found with ID: " + id);
        }
        book.setId(id);
        repository.update(id, book);
        return ResponseEntity.ok(repository.findById(id).get());
    }

    // Delete book by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id) {
        int rows = repository.delete(id);
        if (rows > 0) {
            return ResponseEntity.ok("Book deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found with ID: " + id);
    }
}
