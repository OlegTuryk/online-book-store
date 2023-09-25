package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.order.OrderCreateRequestDto;
import com.app.onlinebookstore.dto.order.OrderDto;
import com.app.onlinebookstore.dto.order.OrderItemDto;
import com.app.onlinebookstore.dto.order.OrderUpdateRequestDto;
import com.app.onlinebookstore.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderDto createOrder(@RequestBody OrderCreateRequestDto orderCreateRequestDto) {
        return orderService.save(orderCreateRequestDto);
    }

    @GetMapping
    public List<OrderDto> getOrders(Pageable pageable) {
        return orderService.getOrderHistory(pageable);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId, Pageable pageable) {
        return orderService.getOrderItems(orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getOrderItem(orderId, itemId);
    }

    @PatchMapping("/{id}")
    public OrderDto updateOrderStatus(@PathVariable Long id,
                                      @RequestBody OrderUpdateRequestDto orderUpdateRequestDto) {
        return orderService.updateStatus(id, orderUpdateRequestDto);
    }
}
