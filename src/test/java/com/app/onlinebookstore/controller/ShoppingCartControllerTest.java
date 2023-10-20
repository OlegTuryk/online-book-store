package com.app.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.app.onlinebookstore.dto.shoppingcart.CreateCartItemDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.app.onlinebookstore.dto.shoppingcart.UpdateCartItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = "classpath:database/create/add-book-and-category.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/create/add-user.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/create/add-shopping-cart-and-cart-item.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/delete/delete-shopping-cart-and-cart-item-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:database/delete/delete-book-and-category-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:database/delete/delete-user-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    private static final Long VALID_ID = 1L;
    private static final Long ANOTHER_VALID_ID = 2L;
    private static final int VALID_QUANTITY = 10;
    private static final String CART_ENDPOINT = "/cart";
    private static CartItemDto cartItemDto;
    private static ShoppingCartDto shoppingCartDto;
    private static UpdateCartItemDto updateCartItemDto;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        CreateCartItemDto createCartItemDto = new CreateCartItemDto();
        createCartItemDto.setBookId(VALID_ID);
        createCartItemDto.setQuantity(VALID_QUANTITY);

        updateCartItemDto = new UpdateCartItemDto();
        updateCartItemDto.setQuantity(15);

        cartItemDto = new CartItemDto();
        cartItemDto.setId(VALID_ID);
        cartItemDto.setBookId(VALID_ID);
        cartItemDto.setBookTitle("Test Title1");
        cartItemDto.setQuantity(VALID_QUANTITY);

        shoppingCartDto = new ShoppingCartDto(VALID_ID, ANOTHER_VALID_ID, List.of(cartItemDto));
    }

    @Test
    @DisplayName("Save cartItem")
    @WithMockUser(username = "user@example.com")
    void createCartItem_validRequestDto_returnShoppingCartDto() throws Exception {
        CreateCartItemDto saveCartItem = new CreateCartItemDto();
        saveCartItem.setBookId(ANOTHER_VALID_ID);
        saveCartItem.setQuantity(VALID_QUANTITY);

        CartItemDto savedCartItem = new CartItemDto();
        savedCartItem.setId(ANOTHER_VALID_ID);
        savedCartItem.setBookId(ANOTHER_VALID_ID);
        savedCartItem.setBookTitle("Test Title2");
        savedCartItem.setQuantity(VALID_QUANTITY);

        String jsonRequest = objectMapper.writeValueAsString(saveCartItem);
        MvcResult result = mockMvc.perform(
                        post(CART_ENDPOINT)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CartItemDto actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), CartItemDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(savedCartItem, actual, "id"));
    }

    @Test
    @DisplayName("Get shoppingCart")
    @WithMockUser(username = "user@example.com")
    void getShoppingCart_returnShoppingCart() throws Exception {
        MvcResult result = mockMvc.perform(
                        get(CART_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartDto actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), ShoppingCartDto.class);

        assertNotNull(actual);
        assertNotNull(actual.id());
        assertTrue(EqualsBuilder.reflectionEquals(shoppingCartDto, actual, "id"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    @DisplayName("Update cartItem by id")
    void update_validRequest_returnsExpectedCartItem() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(CART_ENDPOINT + "/cart-items/1")
                        .content(objectMapper.writeValueAsString(updateCartItemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CartItemDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CartItemDto.class);

        cartItemDto.setQuantity(15);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(cartItemDto, actual, "id"));
        cartItemDto.setQuantity(10);
    }

    @Test
    @WithMockUser(username = "user@example.com")
    @DisplayName("Delete cartItem by valid id")
    void delete_validId_successful() throws Exception {
        mockMvc.perform(delete(CART_ENDPOINT + "/cart-items/1"))
                .andExpect(status().isNoContent());
    }
}
