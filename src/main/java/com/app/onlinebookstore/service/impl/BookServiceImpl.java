package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.book.BookDto;
import com.app.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.app.onlinebookstore.dto.book.BookSearchParametersDto;
import com.app.onlinebookstore.dto.book.CreateBookRequestDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.BookMapper;
import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.repository.BookRepository;
import com.app.onlinebookstore.service.BookService;
import com.app.onlinebookstore.specifications.BookSpecifications;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id)
        ));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto bookDto) {
        Book updatedBook = bookMapper.toModel(bookDto);
        updatedBook.setId(id);
        return bookMapper.toDto(bookRepository.save(updatedBook));
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable) {
        return bookRepository.findAll(
                BookSpecifications.searchBooks(searchParameters), pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id, Pageable pageable) {
        return bookRepository.findAllByCategoriesId(id, pageable).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
