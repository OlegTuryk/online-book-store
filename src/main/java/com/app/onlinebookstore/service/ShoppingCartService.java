package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.app.onlinebookstore.dto.shoppingcart.CreateCartItemDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.app.onlinebookstore.dto.shoppingcart.UpdateCartItemDto;
import com.app.onlinebookstore.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCartDto find();

    CartItemDto save(CreateCartItemDto cartItem);

    CartItemDto update(UpdateCartItemDto updateCartItemDto, Long id);

    void deleteById(Long cartItemId);

    void clearShoppingCart(ShoppingCart shoppingCart);
}
