package com.app.onlinebookstore.dto.shoppingcart;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private int quantity;
}
