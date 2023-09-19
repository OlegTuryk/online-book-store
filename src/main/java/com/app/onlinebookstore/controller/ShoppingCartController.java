package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.shopping_cart.CartItemDto;
import com.app.onlinebookstore.dto.shopping_cart.CreateCartItemDto;
import com.app.onlinebookstore.dto.shopping_cart.ShoppingCartDto;
import com.app.onlinebookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    @GetMapping
    public ShoppingCartDto getAll() {
        return shoppingCartService.find();
    }

    @PostMapping
    public CartItemDto createCartItem(@RequestBody CreateCartItemDto cartItem) {
        return shoppingCartService.save(cartItem);
    }
}
