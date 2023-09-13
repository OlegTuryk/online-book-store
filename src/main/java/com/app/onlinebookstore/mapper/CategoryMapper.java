package com.app.onlinebookstore.mapper;

import com.app.onlinebookstore.dto.category.CategoryDto;
import com.app.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.app.onlinebookstore.model.Category;

public interface CategoryMapper {
    CategoryDto toDto(Category category);
    Category toEntity(CreateCategoryRequestDto categoryRequestDto);
}
