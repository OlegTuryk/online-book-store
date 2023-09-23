package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.category.CategoryDto;
import com.app.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.CategoryMapper;
import com.app.onlinebookstore.model.Category;
import com.app.onlinebookstore.repository.CategoryRepository;
import com.app.onlinebookstore.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id)
        ));
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(categoryDto)));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        Category updatedCategory = categoryMapper.toEntity(categoryDto);
        updatedCategory.setId(id);
        return categoryMapper.toDto(categoryRepository.save(updatedCategory));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
