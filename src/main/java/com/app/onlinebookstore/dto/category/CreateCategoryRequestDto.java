package com.app.onlinebookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;
    @Size(max = 250)
    private String description;
}
