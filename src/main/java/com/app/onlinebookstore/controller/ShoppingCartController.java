package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.app.onlinebookstore.dto.shoppingcart.CreateCartItemDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.app.onlinebookstore.dto.shoppingcart.UpdateCartItemDto;
import com.app.onlinebookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CartItemDto create(@RequestBody CreateCartItemDto cartItem) {
        return shoppingCartService.save(cartItem);
    }

    @PutMapping("/cart-items/{cartItemId}")
    public CartItemDto update(@PathVariable Long cartItemId,
                              @RequestBody UpdateCartItemDto cartItem) {
        return shoppingCartService.update(cartItem, cartItemId);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    public void deleteById(@PathVariable Long cartItemId) {
        shoppingCartService.deleteById(cartItemId);
    }
}
