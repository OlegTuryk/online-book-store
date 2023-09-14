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
import jakarta.transaction.Transactional;
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
    @Transactional
    public CartItemDto save(CreateCartItemDto createCartItemDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userService.getUser().getId())
                .orElseGet(this::createShoppingCart);
        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> createCartItemDto.getBookId().equals(item.getBook().getId()))
                .peek(item -> item.setQuantity(item.getQuantity() + createCartItemDto.getQuantity()))
                .findFirst()
                .orElse(createCartItem(shoppingCart, createCartItemDto));
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    private CartItem createCartItem(ShoppingCart shoppingCart, CreateCartItemDto createCartItemDto) {
        Book book = bookRepository.findById(createCartItemDto.getBookId()).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem.setQuantity(createCartItemDto.getQuantity());
        return cartItem;
    }

    private ShoppingCart createShoppingCart() {
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(userService.getUser());
        return shoppingCartRepository.save(newShoppingCart);
    }
}
