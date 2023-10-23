package com.app.onlinebookstore.mapper;

import com.app.onlinebookstore.config.MapperConfig;
import com.app.onlinebookstore.dto.order.OrderCreateRequestDto;
import com.app.onlinebookstore.dto.order.OrderDto;
import com.app.onlinebookstore.model.Order;
import com.app.onlinebookstore.model.ShoppingCart;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "total", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "shippingAddress", source = "orderCreateRequestDto.shippingAddress")
    @Mapping(target = "orderItems", source = "shoppingCart.cartItems")
    Order toEntity(ShoppingCart shoppingCart, OrderCreateRequestDto orderCreateRequestDto);

    @Mapping(target = "userId", source = "order.user.id")
    OrderDto toDto(Order order);

    @AfterMapping
    default void setOrderParams(@MappingTarget Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(order.getOrderItems().stream()
                       .peek(item -> item.setOrder(order))
                       .map(item -> item.getPrice()
                               .multiply(BigDecimal.valueOf(item.getQuantity())))
                       .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
