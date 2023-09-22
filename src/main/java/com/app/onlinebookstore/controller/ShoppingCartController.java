package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.app.onlinebookstore.dto.shoppingcart.CreateCartItemDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.app.onlinebookstore.dto.shoppingcart.UpdateCartItemDto;
import com.app.onlinebookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ShoppingCart management", description = "Endpoints for managing shoppingCarts and cartItems")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get Shopping Cart", description = "Fetch the current shopping cart")
    @GetMapping
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.find();
    }

    @Operation(summary = "Create Cart Item", description = "Add a new item to the shopping cart")
    @PostMapping
    public CartItemDto createCartItem(@RequestBody CreateCartItemDto cartItem) {
        return shoppingCartService.save(cartItem);
    }

    @Operation(summary = "Update Cart Item", description = "Update an existing item in the shopping cart")
    @PutMapping("/cart-items/{cartItemId}")
    public CartItemDto updateCartItem(@PathVariable Long cartItemId,
                              @RequestBody UpdateCartItemDto cartItem) {
        return shoppingCartService.update(cartItem, cartItemId);
    }

    @Operation(summary = "Delete Cart Item", description = "Remove an item from the shopping cart by its ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cart-items/{cartItemId}")
    public void deleteCartItemById(@PathVariable Long cartItemId) {
        shoppingCartService.deleteById(cartItemId);
    }
}
