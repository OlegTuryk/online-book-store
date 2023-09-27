package com.app.onlinebookstore.repository;

import com.app.onlinebookstore.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
            FROM OrderItem oi
            WHERE oi.order.id = :orderId
            AND oi.order.user.id = :userId
            """)
    List<OrderItem> findOrderItemsByOrderIdAndUserId(Long orderId, Long userId, Pageable pageable);

    @Query("""
            FROM OrderItem oi
            WHERE oi.order.id = :orderId
            AND oi.order.user.id = :userId
            AND oi.id = :orderItemId
            """)
    Optional<OrderItem> findOrderItemByOrderIdAndUserIdAndOrderItemId(
            Long orderId, Long orderItemId, Long userId);
}
