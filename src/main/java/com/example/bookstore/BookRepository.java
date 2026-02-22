package com.example.bookstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByDeleted(Integer deleted);

    @Query("SELECT b FROM Book b WHERE b.deleted = 0" +
            "AND (:title IS NULL OR b.title LIKE %:title%) " +
            "AND (:author IS NULL OR b.author LIKE %:author%) " +
            "AND (:category IS NULL OR b.category LIKE %:category%)")
    List<Book> searchBooks(@Param("title") String title,
                           @Param("author") String author,
                           @Param("category") String category);
}
