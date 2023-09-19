package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.shopping_cart.CartItemDto;
import com.app.onlinebookstore.dto.shopping_cart.CreateCartItemDto;
import com.app.onlinebookstore.dto.shopping_cart.ShoppingCartDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.CartItemMapper;
import com.app.onlinebookstore.mapper.ShoppingCartMapper;
import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.model.CartItem;
import com.app.onlinebookstore.model.ShoppingCart;
import com.app.onlinebookstore.repository.BookRepository;
import com.app.onlinebookstore.repository.CartItemRepository;
import com.app.onlinebookstore.repository.ShoppingCartRepository;
import com.app.onlinebookstore.service.ShoppingCartService;
import com.app.onlinebookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserService userService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto find() {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(userService.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found")));
    }

    @Override
    public CartItemDto save(CreateCartItemDto createCartItemDto) {
        CartItem cartItem = cartItemMapper.toEntity(createCartItemDto);
        setCartItem(cartItem, createCartItemDto, userService.getUser().getId());
        return cartItemMapper.toDto(cartItemRepository
                .save(cartItem));
    }

    private void setCartItem(CartItem cartItem, CreateCartItemDto createCartItemDto, Long userId) {
        if (createCartItemDto.getBookId() != null) {
            Book book = bookRepository.findById(createCartItemDto.getBookId()).orElseThrow(() -> new EntityNotFoundException("Book not found"));
            cartItem.setBook(book);
        }
        if (userId != null) {
            ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                    .orElseGet(this::createShoppingCart);
            cartItem.setShoppingCart(shoppingCart);
            shoppingCartRepository.save(shoppingCart);
        }
    }

    private ShoppingCart createShoppingCart() {
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(userService.getUser());
        return newShoppingCart;
    }
}
