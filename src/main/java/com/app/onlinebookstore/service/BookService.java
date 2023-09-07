package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.BookDto;
import com.app.onlinebookstore.dto.BookSearchParametersDto;
import com.app.onlinebookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getById(Long id);

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookDto);

    List<BookDto> search(BookSearchParametersDto searchParameters);
}
