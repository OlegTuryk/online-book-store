package com.app.onlinebookstore.repository;

import com.app.onlinebookstore.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
          FROM Order o
          LEFT JOIN FETCH o.orderItems oi
          LEFT JOIN FETCH oi.book b
          LEFT JOIN FETCH o.user u
          WHERE u.id = :userId
            """)
    List<Order> findAllByUserId(Pageable pageable, Long userId);

    @Query("""
            FROM Order o
            LEFT JOIN FETCH o.user
            LEFT JOIN FETCH o.orderItems oi
            LEFT JOIN FETCH oi.book
            WHERE o.id = :orderId
            """)
    Optional<Order> findByIdWithAllData(Long orderId);
}
