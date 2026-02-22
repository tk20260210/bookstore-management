package com.example.bookstore;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {

    List<Sales> findAllByOrderBySaleDateDesc();

    @Query("SELECT COALESCE(SUM(s.totalPrice), 0) FROM Sales s WHERE DATE(s.saleDate) = CURRENT_DATE")
    Integer getTodayTotalSales();

    @Query("SELECT s.bookTitle, SUM(s.quantity) as totalQty, SUM(s.totalPrice) as totalSales " +
            "FROM Sales s GROUP BY s.bookId, s.bookTitle ORDER BY totalSales DESC")
    List<Object[]> getBookSalesRanking();
}
