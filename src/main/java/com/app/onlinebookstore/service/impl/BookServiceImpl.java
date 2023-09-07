package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.BookDto;
import com.app.onlinebookstore.dto.CreateBookRequestDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.BookMapper;
import com.app.onlinebookstore.repository.BookRepository;
import com.app.onlinebookstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        return bookMapper
                .toDto(bookRepository.save(bookMapper.toModel(requestDto)));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookMapper.toDto(bookRepository.getBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id)
        ));
    }
}
