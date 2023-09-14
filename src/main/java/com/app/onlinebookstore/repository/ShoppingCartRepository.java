package com.app.onlinebookstore.repository;

import com.app.onlinebookstore.dto.shopping_cart.ShoppingCartDto;
import com.app.onlinebookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query(value = "SELECT sc.id as sc_id, sc.user_id as sc_user_id, sc.is_deleted as sc_is_deleted, ci.id as ci_id, ci.shopping_cart_id as ci_shopping_cart_id, ci.is_deleted as ci_is_deleted " +
            "FROM shopping_carts sc " +
            "LEFT JOIN cart_items ci ON sc.id = ci.shopping_cart_id " +
            "WHERE sc.user_id = :userId ;", nativeQuery = true)
    Optional<ShoppingCart> findByUserId(Long userId);

}
