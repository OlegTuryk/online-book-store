package com.app.onlinebookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.model.CartItem;
import com.app.onlinebookstore.model.Category;
import com.app.onlinebookstore.model.Role;
import com.app.onlinebookstore.model.ShoppingCart;
import com.app.onlinebookstore.model.User;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:database/create/add-user.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/create/add-book-and-category.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/create/add-shopping-cart-and-cart-item.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/delete/delete-shopping-cart-and-cart-item-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:database/delete/delete-book-and-category-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:database/delete/delete-user-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    private static ShoppingCart testShoppingCart;
    private static final Long VALID_ID = 1L;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @BeforeAll
    public static void setUp() {
        User testUser = new User();
        testUser.setId(2L);
        testUser.setEmail("admin@example.com");
        testUser.setPassword("$2a$12$YidPrKXOD3Bb6wcgh8WcNeXX8D4MLhR.bPZ0/0scXIoBQMTrLZRmi");
        testUser.setFirstName("Oleg");
        testUser.setLastName("Turyk");
        testUser.setShippingAddress("userAddress");
        Role roleUser = new Role();
        roleUser.setName(Role.RoleName.ROLE_ADMIN);
        testUser.setRoles(Set.of(roleUser));

        Category testCategory1 = new Category();
        testCategory1.setId(VALID_ID);
        testCategory1.setName("Test Category 1");
        testCategory1.setDescription("Description for Test Category 1");

        Book testBook = new Book();
        testBook.setId(VALID_ID);
        testBook.setTitle("Test Title1");
        testBook.setAuthor("Test Author1");
        testBook.setIsbn("978-3-16-148410-0");
        testBook.setPrice(BigDecimal.valueOf(19.99));
        testBook.setDescription("Test Description1");
        testBook.setCoverImage("Test Cover Image1");
        testBook.setCategories(Set.of());

        CartItem testCartItem = new CartItem();
        testCartItem.setId(VALID_ID);
        testCartItem.setBook(testBook);
        testCartItem.setQuantity(10);

        testShoppingCart = new ShoppingCart();
        testShoppingCart.setId(VALID_ID);
        testShoppingCart.setUser(testUser);
        testShoppingCart.setCartItems(Set.of(testCartItem));
    }

    @Test
    public void findByUserId_validId_returnShoppingCart() {
        Optional<ShoppingCart> actualShoppingCart = shoppingCartRepository.findByUserId(2L);
        assertTrue(actualShoppingCart.isPresent());
        assertEquals(testShoppingCart, actualShoppingCart.get());
    }
}
