package com.app.onlinebookstore.repository;

import com.app.onlinebookstore.dto.shopping_cart.ShoppingCartDto;
import com.app.onlinebookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("FROM ShoppingCart sc "
            + "LEFT JOIN FETCH sc.cartItems ci "
            + "LEFT JOIN FETCH sc.user u WHERE u.id = :userId ")
    Optional<ShoppingCart> findByUserId(Long userId);
}
