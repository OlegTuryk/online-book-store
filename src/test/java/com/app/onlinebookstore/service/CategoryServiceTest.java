package com.app.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.onlinebookstore.dto.category.CategoryDto;
import com.app.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.CategoryMapper;
import com.app.onlinebookstore.model.Category;
import com.app.onlinebookstore.repository.CategoryRepository;
import com.app.onlinebookstore.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    private static Category category;
    private static CategoryDto categoryDto;
    private static final Long TEST_ID = 1L;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeAll
    public static void setUp() {
        category = new Category();
        category.setId(TEST_ID);
        category.setName("fiction");

        categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
    }

    @Test
    @DisplayName("Find all categories")
    public void findAll_ReturnAllCategories() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<Category> categoryPage =
                new PageImpl<>(List.of(category), pageable, 1);
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        List<CategoryDto> actual = categoryService.findAll(pageable);
        assertEquals(List.of(categoryDto), actual);
    }

    @Test
    @DisplayName("Save category")
    public void save_ValidCategory_ReturnSavedCategoryDto() {
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto();
        createCategoryRequestDto.setName("fiction");

        when(categoryMapper.toEntity(createCategoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.save(createCategoryRequestDto);
        assertNotNull(actual);
        assertEquals(categoryDto, actual);
    }

    @Test
    @DisplayName("Update category")
    public void update_CategoryExists_ReturnUpdatedCategoryDto() {
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto();
        createCategoryRequestDto.setName("fiction");
        Category updatedCategory = new Category();
        updatedCategory.setId(TEST_ID);
        updatedCategory.setName("fiction");

        when(categoryMapper.toEntity(createCategoryRequestDto)).thenReturn(updatedCategory);
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.update(TEST_ID, createCategoryRequestDto);
        assertNotNull(actual);
        assertEquals(categoryDto, actual);
    }

    @Test
    @DisplayName("Get category by id")
    public void getById_CategoryExists_ReturnCategoryDto() {
        when(categoryRepository.findById(TEST_ID)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        CategoryDto actual = categoryService.getById(TEST_ID);
        assertNotNull(actual);
        assertEquals(categoryDto, actual);
    }

    @Test
    @DisplayName("Delete category by id")
    public void deleteById_CategoryExists_DeleteSuccessfully() {
        doNothing().when(categoryRepository).deleteById(TEST_ID);
        categoryService.deleteById(TEST_ID);
        verify(categoryRepository, times(1)).deleteById(TEST_ID);
    }

    @Test
    @DisplayName("Get category by id - Category not found")
    public void getById_CategoryNotFound_ThrowEntityNotFoundException() {
        Long nonExistentId = 2L;
        when(categoryRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(nonExistentId);
        });
    }
}
