package com.example.bookstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findAllByOrderByPurchaseDateDesc();

    @Query(value = "SELECT COALESCE(SUM(total_price),0) FROM purchases WHERE DATE(purchase_date) = CURRENT_DATE",
            nativeQuery = true)
    Integer getTodayTotalPurchases();

    //===== Query(for statistic) =====

    //Total Purchases
    @Query(value = "SELECT COALESCE(SUM(total_price), 0) FROM purchases",
            nativeQuery = true)
    Integer getTotalPurchases();
}
