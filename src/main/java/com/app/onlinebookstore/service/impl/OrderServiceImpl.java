package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.order.OrderCreateRequestDto;
import com.app.onlinebookstore.dto.order.OrderDto;
import com.app.onlinebookstore.dto.order.OrderItemDto;
import com.app.onlinebookstore.dto.order.OrderUpdateRequestDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.OrderItemMapper;
import com.app.onlinebookstore.mapper.OrderMapper;
import com.app.onlinebookstore.model.Order;
import com.app.onlinebookstore.model.ShoppingCart;
import com.app.onlinebookstore.model.User;
import com.app.onlinebookstore.repository.OrderItemRepository;
import com.app.onlinebookstore.repository.OrderRepository;
import com.app.onlinebookstore.repository.ShoppingCartRepository;
import com.app.onlinebookstore.service.OrderService;
import com.app.onlinebookstore.service.ShoppingCartService;
import com.app.onlinebookstore.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public OrderDto save(OrderCreateRequestDto orderCreateRequestDto) {
        User user = userService.getUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart by username: "
                                + user.getUsername()));
        Order order = orderMapper.toEntity(shoppingCart, orderCreateRequestDto);
        shoppingCartService.clearShoppingCart(shoppingCart);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getOrderHistory(Pageable pageable) {
        return orderRepository.findAllByUserId(pageable, userService.getUser().getId()).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId, Pageable pageable) {
        return orderItemRepository.findOrderItemsByOrderIdAndUserId(orderId,
                userService.getUser().getId(), pageable).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItem(Long orderId, Long itemId) {
        return orderItemMapper.toDto(orderItemRepository
                .findOrderItemByOrderIdAndUserIdAndOrderItemId(
                        orderId, itemId, userService.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find orderItem by id: " + itemId
                )));
    }

    @Override
    public OrderDto updateStatus(Long id, OrderUpdateRequestDto orderUpdateRequestDto) {
        Order order = orderRepository.findByIdWithAllData(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by id: " + id
                ));
        order.setStatus(orderUpdateRequestDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }
}
