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
    private static final Book BOOK1 = new Book();
    private static final Book BOOK2 = new Book();
    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    public static void setUp() {
        Category testCategory1 = new Category();
        testCategory1.setId(1L);
        testCategory1.setName("Test Category 1");
        testCategory1.setDescription("Description for Test Category 1");
        Category testCategory2 = new Category();
        testCategory2.setId(2L);
        testCategory2.setName("Test Category 2");
        testCategory2.setDescription("Description for Test Category 2");

        BOOK1.setId(1L);
        BOOK1.setTitle("Test Title1");
        BOOK1.setAuthor("Test Author1");
        BOOK1.setIsbn("978-3-16-148410-0");
        BOOK1.setPrice(BigDecimal.valueOf(19.99));
        BOOK1.setDescription("Test Description1");
        BOOK1.setCoverImage("Test Cover Image1");
        BOOK1.setCategories(Set.of(testCategory1));
        BOOK2.setId(2L);
        BOOK2.setTitle("Test Title2");
        BOOK2.setAuthor("Test Author2");
        BOOK2.setIsbn("978-0-14-103614-4");
        BOOK2.setPrice(BigDecimal.valueOf(19.99));
        BOOK2.setDescription("Test Description2");
        BOOK2.setCoverImage("Test Cover Image2");
        BOOK2.setCategories(Set.of(testCategory2));
    }

    @Test
    @DisplayName("Find book by id with categories")
    public void findByIdWithCategories_validBookId_returnBook() {
        Optional<Book> bookOptional = bookRepository.findByIdWithCategories(BOOK1.getId());

        assertTrue(bookOptional.isPresent());
        Book bookFromDb = bookOptional.get();
        assertEquals(BOOK1, bookFromDb);
    }

    @Test
    @DisplayName("Find all books with categories")
    public void findAllWithCategories_returnAllBooksWithCategories() {

        Pageable pageable = PageRequest.of(0, 5);
        List<Book> books = bookRepository.findAllWithCategories(pageable);
        assertEquals(List.of(BOOK1, BOOK2), books);
    }

    @Test
    @DisplayName("Find all books by category id")
    public void findAllByCategoriesId_ValidCategoryId_ReturnBooks() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Book> books = bookRepository.findAllByCategoriesId(1L, pageable);
        assertEquals(List.of(BOOK1), books);
    }
}
