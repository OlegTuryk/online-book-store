package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.app.onlinebookstore.dto.shoppingcart.CreateCartItemDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.app.onlinebookstore.dto.shoppingcart.UpdateCartItemDto;

public interface ShoppingCartService {
    ShoppingCartDto find();

    CartItemDto save(CreateCartItemDto cartItem);

    CartItemDto update(UpdateCartItemDto updateCartItemDto, Long id);

    void deleteById(Long cartItemId);
}
