package com.app.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.onlinebookstore.dto.book.BookDto;
import com.app.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.app.onlinebookstore.dto.book.BookSearchParametersDto;
import com.app.onlinebookstore.dto.book.CreateBookRequestDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.BookMapper;
import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.repository.BookRepository;
import com.app.onlinebookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
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
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static Book book;
    private static BookDto bookDto;
    private static BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds;
    private static final Long TEST_ID = 1L;
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 2;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeAll
    public static void setUp() {
        book = new Book();
        book.setId(TEST_ID);
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890123");
        book.setPrice(new BigDecimal("19.99"));
        book.setDescription("Test Description");
        book.setCoverImage("Test Cover Image");

        bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());

        bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setId(book.getId());
        bookDtoWithoutCategoryIds.setTitle(book.getTitle());
        bookDtoWithoutCategoryIds.setAuthor(book.getAuthor());
        bookDtoWithoutCategoryIds.setIsbn(book.getIsbn());
        bookDtoWithoutCategoryIds.setPrice(book.getPrice());
        bookDtoWithoutCategoryIds.setDescription(book.getDescription());
        bookDtoWithoutCategoryIds.setCoverImage(book.getCoverImage());
    }

    @Test
    @DisplayName("Save book - Valid book")
    public void save_ValidBook_ReturnSavedBookDto() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Test Title");
        createBookRequestDto.setAuthor("Test Author");
        createBookRequestDto.setIsbn("978-3-16-148410-0");
        createBookRequestDto.setPrice(new BigDecimal("19.99"));

        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        BookDto actual = bookService.save(createBookRequestDto);

        assertNotNull(actual);
        assertEquals(bookDto, actual);
    }

    @Test
    @DisplayName("Find all books")
    public void findAll_ReturnAllBooks() {
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<Book> books = List.of(book);

        when(bookRepository.findAllWithCategories(pageable)).thenReturn(books);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        List<BookDto> actual = bookService.findAll(pageable);

        assertEquals(List.of(bookDto), actual);
    }

    @Test
    @DisplayName("Get book by id - Book exists")
    public void getById_BookExists_ReturnBookDto() {
        when(bookRepository.findByIdWithCategories(TEST_ID)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        BookDto actual = bookService.getById(TEST_ID);

        assertNotNull(actual);
        assertEquals(bookDto, actual);
    }

    @Test
    @DisplayName("Get book by id - Book not found")
    public void getById_BookNotFound_ThrowEntityNotFoundException() {
        Long nonExistentId = 2L;

        when(bookRepository.findByIdWithCategories(nonExistentId)).thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> bookService.getById(nonExistentId));
        assertEquals("Can't find book by id: " + nonExistentId, exception.getMessage());
    }

    @Test
    @DisplayName("Delete book by id")
    public void deleteById_BookExists_DeleteSuccessfully() {
        doNothing().when(bookRepository).deleteById(TEST_ID);
        bookService.deleteById(TEST_ID);

        verify(bookRepository).deleteById(TEST_ID);
    }

    @Test
    @DisplayName("Update book - Book exists")
    public void update_BookExists_ReturnUpdatedBookDto() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Updated Title");
        Book updatedBook = new Book();
        updatedBook.setId(TEST_ID);
        updatedBook.setTitle(createBookRequestDto.getTitle());
        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setId(TEST_ID);
        updatedBookDto.setAuthor(book.getTitle());

        when(bookMapper.toModel(createBookRequestDto)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(bookDto);
        BookDto actual = bookService.update(TEST_ID, createBookRequestDto);

        assertNotNull(actual);
        assertEquals(bookDto, actual);
    }

    @Test
    @DisplayName("Find all books by category id")
    public void findAllByCategoryId_CategoryExists_ReturnBooks() {
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<Book> books = List.of(book);

        when(bookRepository.findAllByCategoriesId(TEST_ID, pageable)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);
        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(TEST_ID, pageable);

        assertEquals(List.of(bookDtoWithoutCategoryIds), actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Search books - Valid search parameters")
    public void search_ValidSearchParameters_ReturnBooks() {
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        BookSearchParametersDto searchParameters = new BookSearchParametersDto();
        searchParameters.setTitle("Test Title");
        Page<Book> bookPage =
                new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findAll(any(Specification.class),
                any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        List<BookDto> actual = bookService.search(searchParameters, pageable);

        assertEquals(List.of(bookDto), actual);
    }
}
