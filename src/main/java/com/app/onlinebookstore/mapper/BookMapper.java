package com.app.onlinebookstore.mapper;

import com.app.onlinebookstore.config.MapperConfig;
import com.app.onlinebookstore.dto.book.BookDto;
import com.app.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.app.onlinebookstore.dto.book.CreateBookRequestDto;
import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "categories", source = "categoryIds")
    Book toModel(CreateBookRequestDto bookDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoryIds(book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }

    default Set<Category> mapIdsToCategories(Set<Long> ids) {
        return ids.stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());
    }
}
