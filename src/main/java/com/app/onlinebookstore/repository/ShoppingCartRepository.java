package com.app.onlinebookstore.repository;

import com.app.onlinebookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query(""" 
            FROM ShoppingCart sc
            LEFT JOIN FETCH sc.cartItems ci
            LEFT JOIN FETCH ci.book b
            WHERE sc.user.id = :userId
            """)
    Optional<ShoppingCart> findByUserId(Long userId);

}
