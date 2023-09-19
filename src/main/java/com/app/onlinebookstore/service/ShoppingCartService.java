package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.shopping_cart.CartItemDto;
import com.app.onlinebookstore.dto.shopping_cart.CreateCartItemDto;
import com.app.onlinebookstore.dto.shopping_cart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto find();

    CartItemDto save(CreateCartItemDto cartItem);
}
