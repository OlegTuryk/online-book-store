package com.app.onlinebookstore.mapper;

import com.app.onlinebookstore.config.MapperConfig;
import com.app.onlinebookstore.dto.BookDto;
import com.app.onlinebookstore.dto.CreateBookRequestDto;
import com.app.onlinebookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookDto);
}
