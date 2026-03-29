package com.example.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookApiController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/book")
    public List<Book> getBooks() {
        return bookRepository.findByDeleted(0);
    }

    @GetMapping("/book/{id}")
    public Book getBook(@PathVariable Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    //Register
    @PostMapping("/books")
    public Book createBook(@RequestBody Book book) {
        book.setDeleted(0);
        return bookRepository.save(book);
    }

    //Update
    @PostMapping("/books/{id}")
        public Book updateBook(@PathVariable Integer id, @RequestBody Book book) {
        book.setId(id);
        book.setDeleted(0);
        return bookRepository.save(book);
    }

    //Delete(logical)
    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Integer id) {
        bookRepository.findById(id).ifPresent(b -> {
            b.setDeleted(1);
            bookRepository.save(b);
        });
    }

}
