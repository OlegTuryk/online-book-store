package com.app.onlinebookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.model.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/create/add-book-and-category.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/delete/delete-book-and-category-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookRepositoryTest {
    private static final Long TEST_ID = 1L;
    private static Book testBook;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeAll
    public static void setUp() {
        Category testCategory = new Category();
        testCategory.setId(TEST_ID);
        testCategory.setName("Test Category");

        testBook = new Book();
        testBook.setId(TEST_ID);
        testBook.setTitle("Test Title");
        testBook.setAuthor("Test Author");
        testBook.setIsbn("1234567890123");
        testBook.setPrice(BigDecimal.valueOf(19.99));
        testBook.setDescription("Test Description");
        testBook.setCoverImage("Test Cover Image");
        testBook.setCategories(Set.of(testCategory));
    }

    @Test
    @DisplayName("Find book by id with categories")
    public void findByIdWithCategories_ValidBookId_ReturnBook() {
        Optional<Book> bookOptional = bookRepository.findByIdWithCategories(testBook.getId());

        assertTrue(bookOptional.isPresent());
        Book bookFromDb = bookOptional.get();
        assertEquals(testBook, bookFromDb);
    }

    @Test
    @DisplayName("Find all books with categories")
    public void findAllWithCategories_ReturnAllBooksWithCategories() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Book> books = bookRepository.findAllWithCategories(pageable);
        assertEquals(List.of(testBook), books);
    }

    @Test
    @DisplayName("Find all books by category id")
    public void findAllByCategoriesId_ValidCategoryId_ReturnBooks() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Book> books = bookRepository.findAllByCategoriesId(TEST_ID, pageable);
        assertEquals(List.of(testBook), books);
    }
}
