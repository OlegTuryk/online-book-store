package com.app.onlinebookstore.dto.shopping_cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemDto {
    @NotNull
    @Min(0)
    private Integer quantity;
}
