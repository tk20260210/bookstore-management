package com.example.bookstore;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {

    List<Sales> findAllByOrderBySaleDateDesc();

    @Query(value = "SELECT COALESCE(SUM(total_price), 0) FROM sales WHERE DATE(sale_date) = CURRENT_DATE",
            nativeQuery = true)
    Integer getTodayTotalSales();

    @Query(value = "SELECT book_title, SUM(quantity) as totalQty, SUM(total_price) as totalSales " +
            "FROM sales GROUP BY book_id, book_title ORDER BY totalSales DESC",
            nativeQuery = true)
    List<Object[]> getBookSalesRanking();

    //======= Query (Statistic) ======

    //Salse by category
    @Query(value = "SELECT b.category, SUM(s.total_price) " +
            "FROM sales s JOIN book b ON s.book_id = b.id " +
            "GROUP BY b.category " +
            "ORDER BY SUM(s.total_price) DESC",
            nativeQuery = true)
    List<Object[]> getCategorySales();

    //Sales performance by monthly (back in 6 month)
    @Query(value = "SELECT YEAR(sale_date), MONTH(sale_date), SUM(total_price) " +
            "FROM sales " +
            "WHERE sale_date >= DATE_SUB(CURRENT_DATE, INTERVAL 6 MONTH) " +
            "GROUP BY YEAR(sale_date), MONTH(sale_date) " +
            "ORDER BY YEAR(sale_date), MONTH(sale_date)",
            nativeQuery = true)
    List<Object[]> getMonthlySales();

    //Total sales
    @Query(value = "SELECT COALESCE(SUM(total_price), 0) FROM sales ",
            nativeQuery = true)
    Integer getTotalSales();
}
