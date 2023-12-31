package com.app.onlinebookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartItemDto {
    @NotNull
    private Long bookId;
    @NotNull
    @Min(1)
    private Integer quantity;
}
