package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.order.OrderCreateRequestDto;
import com.app.onlinebookstore.dto.order.OrderDto;
import com.app.onlinebookstore.dto.order.OrderItemDto;
import com.app.onlinebookstore.dto.order.OrderUpdateRequestDto;
import com.app.onlinebookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management",
        description = "Endpoints for managing orders and orderItems")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Create a new order",
            description = "Create a new order and save it to the database")
    @PostMapping
    public OrderDto createOrder(@RequestBody OrderCreateRequestDto orderCreateRequestDto) {
        return orderService.save(orderCreateRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all orders",
            description = "Get a list of all orders for the current user")
    @GetMapping
    public List<OrderDto> getOrders(Pageable pageable) {
        return orderService.getOrderHistory(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all items of an order",
            description = "Get a list of all items for a specific order")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId, Pageable pageable) {
        return orderService.getOrderItems(orderId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get a specific item of an order",
            description = "Get a specific item from a specific order")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getOrderItem(orderId, itemId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update the status of an order",
            description = "Update the status of an existing order")
    @PatchMapping("/{id}")
    public OrderDto updateOrderStatus(@PathVariable Long id,
                                      @RequestBody OrderUpdateRequestDto orderUpdateRequestDto) {
        return orderService.updateStatus(id, orderUpdateRequestDto);
    }
}
