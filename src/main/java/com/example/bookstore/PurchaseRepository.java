package com.example.bookstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findAllByOrderByPurchaseDateDesc();

    @Query("SELECT COALESCE(SUM(p.totalPrice),0) FROM Purchase p WHERE DATE(p.purchaseDate) = CURRENT_DATE")
    Integer getTodayTotalPurchases();
}
