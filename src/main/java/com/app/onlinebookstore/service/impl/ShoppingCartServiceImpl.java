package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.app.onlinebookstore.dto.shoppingcart.CreateCartItemDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.app.onlinebookstore.dto.shoppingcart.UpdateCartItemDto;
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
        return shoppingCartMapper.toDto(shoppingCartRepository
                .findByUserId(userService.getUser().getId())
                .orElseGet(this::createShoppingCart));
    }

    @Override
    @Transactional
    public CartItemDto save(CreateCartItemDto createCartItemDto) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(userService.getUser().getId())
                .orElseGet(this::createShoppingCart);
        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(createCartItemDto.getBookId()))
                .peek(item -> {
                    item.setQuantity(createCartItemDto.getQuantity());
                    item.setDeleted(false);
                })
                .findFirst().orElseGet(() -> createCartItem(createCartItemDto));
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Transactional
    @Override
    public CartItemDto update(UpdateCartItemDto updateCartItemDto, Long id) {
        CartItem cartItem = findCartItemByIdAndUserId(id);
        cartItem.setQuantity(updateCartItemDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void clearShoppingCart(ShoppingCart shoppingCart) {
        cartItemRepository.deleteAll(shoppingCart.getCartItems());
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.delete(shoppingCart);
    }
  
    @Transactional
    @Override
    public void deleteById(Long id) {
        cartItemRepository.delete(findCartItemByIdAndUserId(id));
    }

    private CartItem createCartItem(CreateCartItemDto createCartItemDto) {
        CartItem cartItem = new CartItem();
        if (createCartItemDto.getBookId() != null) {
            Book book = bookRepository.findById(createCartItemDto.getBookId())
                    .orElseThrow(() -> new EntityNotFoundException("Book not found"));
            cartItem.setBook(book);
        }
        if (userService.getUser().getId() != null) {
            ShoppingCart shoppingCart = shoppingCartRepository
                    .findByUserId(userService.getUser().getId())
                    .orElseGet(this::createShoppingCart);
            cartItem.setShoppingCart(shoppingCart);
        }
        cartItem.setQuantity(createCartItemDto.getQuantity());
        return cartItem;
    }

    private ShoppingCart createShoppingCart() {
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(userService.getUser());
        return shoppingCartRepository.save(newShoppingCart);
    }

    private CartItem findCartItemByIdAndUserId(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find CartItem by id: " + id
                ));
        if (!cartItem.getShoppingCart().getUser().getId().equals(userService.getUser().getId())) {
            throw new EntityNotFoundException("The cartItem with id: "
                    + id + " doesn't belong to this user");
        }
        return cartItem;
    }
}
