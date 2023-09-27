package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.order.OrderCreateRequestDto;
import com.app.onlinebookstore.dto.order.OrderDto;
import com.app.onlinebookstore.dto.order.OrderItemDto;
import com.app.onlinebookstore.dto.order.OrderUpdateRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto save(OrderCreateRequestDto orderCreateRequestDto);

    List<OrderDto> getOrderHistory(Pageable pageable);

    List<OrderItemDto> getOrderItems(Long orderId, Pageable pageable);

    OrderItemDto getOrderItem(Long orderId, Long itemId);

    OrderDto updateStatus(Long id, OrderUpdateRequestDto orderUpdateRequestDto);
}
