package com.app.onlinebookstore.mapper;

import com.app.onlinebookstore.config.MapperConfig;
import com.app.onlinebookstore.dto.category.CategoryDto;
import com.app.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.app.onlinebookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto categoryRequestDto);
}
