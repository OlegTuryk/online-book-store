package com.app.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.app.onlinebookstore.model.User;
import com.app.onlinebookstore.repository.BookRepository;
import com.app.onlinebookstore.repository.CartItemRepository;
import com.app.onlinebookstore.repository.ShoppingCartRepository;
import com.app.onlinebookstore.service.impl.ShoppingCartServiceImpl;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    private static final Long VALID_ID = 1L;
    private static final int VALID_QUANTITY = 10;
    private static CartItemDto cartItemDto;
    private static CreateCartItemDto createCartItemDto;
    private static ShoppingCartDto shoppingCartDto;
    private static UpdateCartItemDto updateCartItemDto;
    @Mock
    private static User user = new User();
    @Mock
    private static Book book = new Book();
    @Mock
    private UserService userService;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeEach
    void setUp() {
        createCartItemDto = new CreateCartItemDto();
        createCartItemDto.setBookId(VALID_ID);
        createCartItemDto.setQuantity(VALID_QUANTITY);

        updateCartItemDto = new UpdateCartItemDto();
        updateCartItemDto.setQuantity(15);

        cartItemDto = new CartItemDto();
        cartItemDto.setId(VALID_ID);
        cartItemDto.setBookId(VALID_ID);
        cartItemDto.setBookTitle("Test Title");
        cartItemDto.setQuantity(VALID_QUANTITY);

        shoppingCartDto = new ShoppingCartDto(VALID_ID, VALID_ID, List.of(cartItemDto));
    }

    @Test
    @DisplayName("Get shoppingCart by id - ShoppingCart exists")
    public void getById_shoppingCartExists_ReturnShoppingCartDto() {
        ShoppingCart shoppingCart = new ShoppingCart();

        when(shoppingCartRepository.findByUserId(VALID_ID)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);
        when(userService.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(VALID_ID);
        ShoppingCartDto actual = shoppingCartService.find();

        assertNotNull(actual);
        assertEquals(shoppingCartDto, actual);
    }

    @Test
    @DisplayName("Get shoppingCart by id - ShoppingCart does not exist")
    public void getById_shoppingCartDoesNotExist_CreateAndReturnShoppingCartDto() {
        ShoppingCart shoppingCart = new ShoppingCart();

        when(shoppingCartRepository.findByUserId(VALID_ID)).thenReturn(Optional.empty());
        when(userService.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(VALID_ID);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);
        ShoppingCartDto actual = shoppingCartService.find();

        assertNotNull(actual);
        assertEquals(shoppingCartDto, actual);
    }

    @Test
    @DisplayName("Save cartItem - CartItem does not exist")
    public void saveCartItemWhenItDoesntExist_ValidCartItem_ReturnSavedCartItemDto() {
        ShoppingCart shoppingCart = new ShoppingCart();
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(VALID_QUANTITY);
        shoppingCart.setCartItems(Set.of(cartItem));

        when(shoppingCartRepository.findByUserId(VALID_ID)).thenReturn(Optional.of(shoppingCart));
        when(userService.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(VALID_ID);
        when(book.getId()).thenReturn(2L);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDto);
        when(bookRepository.findById(createCartItemDto.getBookId())).thenReturn(Optional.of(book));
        CartItemDto actual = shoppingCartService.save(createCartItemDto);

        assertNotNull(actual);
        assertEquals(cartItemDto, actual);
    }

    @Test
    @DisplayName("Save cartItem - CartItem exist")
    public void saveCartItemWhenItExist_ValidCartItem_ReturnSavedCartItemDto() {
        ShoppingCart shoppingCart = new ShoppingCart();
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        shoppingCart.setCartItems(Set.of(cartItem));

        when(shoppingCartRepository.findByUserId(VALID_ID)).thenReturn(Optional.of(shoppingCart));
        when(userService.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(VALID_ID);
        when(book.getId()).thenReturn(VALID_ID);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDto);
        CartItemDto actual = shoppingCartService.save(createCartItemDto);

        assertNotNull(actual);
        assertEquals(cartItemDto, actual);
    }

    @Test
    @DisplayName("Update cartItem - CartItem exists")
    public void update_CartItemExists_ReturnUpdatedCartItemDto() {
        CartItem cartItem = new CartItem();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        cartItem.setShoppingCart(shoppingCart);

        when(cartItemRepository.findById(VALID_ID)).thenReturn(Optional.of(cartItem));
        when(userService.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(VALID_ID);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDto);
        CartItemDto actual = shoppingCartService.update(updateCartItemDto, VALID_ID);

        assertNotNull(actual);
        assertEquals(cartItemDto, actual);
    }

    @Test
    @DisplayName("Update cartItem - CartItem exists")
    public void update_CartItemDoesntBelongToUser_ReturnEntityNotFoundException() {
        CartItem cartItem = new CartItem();
        ShoppingCart shoppingCart = new ShoppingCart();
        User testUser = new User();
        testUser.setId(VALID_ID);
        shoppingCart.setUser(user);
        cartItem.setShoppingCart(shoppingCart);

        when(cartItemRepository.findById(VALID_ID)).thenReturn(Optional.of(cartItem));
        when(userService.getUser()).thenReturn(testUser);

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                    () -> shoppingCartService.update(updateCartItemDto, VALID_ID));
        assertEquals("The cartItem with id: "
                + VALID_ID + " doesn't belong to this user", exception.getMessage());
    }

    @Test
    @DisplayName("Delete book by id")
    public void deleteById_CartItemExists_DeleteSuccessfully() {
        CartItem cartItem = new CartItem();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        cartItem.setShoppingCart(shoppingCart);

        when(userService.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(VALID_ID);
        when(cartItemRepository.findById(VALID_ID)).thenReturn(Optional.of(cartItem));
        doNothing().when(cartItemRepository).delete(cartItem);
        shoppingCartService.deleteById(VALID_ID);
        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    @DisplayName("Clear shoppingCart")
    public void clearShoppingCart_ShoppingCartExists_ShoppingCartClearedSuccessfully() {
        ShoppingCart shoppingCart = new ShoppingCart();
        CartItem cartItem1 = new CartItem();
        CartItem cartItem2 = new CartItem();
        cartItem1.setId(VALID_ID);
        cartItem2.setId(2L);
        Set<CartItem> cartItems = new HashSet<>(Arrays.asList(cartItem1, cartItem2));
        shoppingCart.setCartItems(cartItems);

        doNothing().when(cartItemRepository).deleteAll(cartItems);
        doNothing().when(shoppingCartRepository).delete(shoppingCart);
        shoppingCartService.clearShoppingCart(shoppingCart);

        verify(cartItemRepository).deleteAll(cartItems);
        verify(shoppingCartRepository).delete(shoppingCart);
        assertTrue(shoppingCart.getCartItems().isEmpty());
    }
}
