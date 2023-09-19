package com.app.onlinebookstore.dto.shopping_cart;

import java.util.List;

public record ShoppingCartDto(Long id, Long userId, List<CartItemDto> cartItems) {
}
