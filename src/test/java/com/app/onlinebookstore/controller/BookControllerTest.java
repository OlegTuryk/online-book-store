package com.app.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.onlinebookstore.dto.book.BookDto;
import com.app.onlinebookstore.dto.book.CreateBookRequestDto;
import com.app.onlinebookstore.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = "classpath:database/create/add-book-and-category.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/delete/delete-book-and-category-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final CreateBookRequestDto CREATE_BOOK_REQUEST_DTO = new CreateBookRequestDto();
    private static final BookDto BOOK_DTO = new BookDto();
    private static final Category CATEGORY = new Category();
    private static final Long VALID_ID = 1L;
    private static final String BOOK_ENDPOINT = "/books";
    private static final String ID_FOR_ENDPOINTS = "/1";
    private static final String USER = "ADMIN";
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        CATEGORY.setId(VALID_ID);
        CATEGORY.setName("Test Category 1");
        CATEGORY.setDescription("Description for Test Category 1");

        CREATE_BOOK_REQUEST_DTO.setTitle("Test Title1");
        CREATE_BOOK_REQUEST_DTO.setAuthor("Test Author1");
        CREATE_BOOK_REQUEST_DTO.setCategoryIds(Set.of(VALID_ID));
        CREATE_BOOK_REQUEST_DTO.setIsbn("978-3-16-148410-0");
        CREATE_BOOK_REQUEST_DTO.setPrice(BigDecimal.valueOf(19.99));
        CREATE_BOOK_REQUEST_DTO.setDescription("Test Description1");
        CREATE_BOOK_REQUEST_DTO.setCoverImage("Test Cover Image1");

        BOOK_DTO.setId(VALID_ID);
        BOOK_DTO.setTitle(CREATE_BOOK_REQUEST_DTO.getTitle());
        BOOK_DTO.setAuthor(CREATE_BOOK_REQUEST_DTO.getAuthor());
        BOOK_DTO.setCategoryIds(CREATE_BOOK_REQUEST_DTO.getCategoryIds());
        BOOK_DTO.setIsbn(CREATE_BOOK_REQUEST_DTO.getIsbn());
        BOOK_DTO.setPrice(CREATE_BOOK_REQUEST_DTO.getPrice());
        BOOK_DTO.setDescription(CREATE_BOOK_REQUEST_DTO.getDescription());
        BOOK_DTO.setCoverImage(CREATE_BOOK_REQUEST_DTO.getCoverImage());
    }

    @Test
    @WithMockUser(username = USER, roles = {USER})
    @DisplayName("Create a new book")
    void createBook_validRequestDto_returnBookDto() throws Exception {
        CreateBookRequestDto saveBookRequestDto = new CreateBookRequestDto();
        saveBookRequestDto.setTitle(CREATE_BOOK_REQUEST_DTO.getTitle());
        saveBookRequestDto.setAuthor(CREATE_BOOK_REQUEST_DTO.getAuthor());
        saveBookRequestDto.setCategoryIds(CREATE_BOOK_REQUEST_DTO.getCategoryIds());
        saveBookRequestDto.setIsbn("978-1-23-456789-7");
        saveBookRequestDto.setPrice(CREATE_BOOK_REQUEST_DTO.getPrice());
        saveBookRequestDto.setDescription(CREATE_BOOK_REQUEST_DTO.getDescription());
        saveBookRequestDto.setCoverImage(CREATE_BOOK_REQUEST_DTO.getCoverImage());
        BookDto bookDto = new BookDto();
        bookDto.setTitle(saveBookRequestDto.getTitle());
        bookDto.setAuthor(saveBookRequestDto.getAuthor());
        bookDto.setCategoryIds(saveBookRequestDto.getCategoryIds());
        bookDto.setIsbn(saveBookRequestDto.getIsbn());
        bookDto.setPrice(saveBookRequestDto.getPrice());
        bookDto.setDescription(saveBookRequestDto.getDescription());
        bookDto.setCoverImage(saveBookRequestDto.getCoverImage());

        String jsonRequest = objectMapper.writeValueAsString(saveBookRequestDto);
        MvcResult result = mockMvc.perform(
                        post(BOOK_ENDPOINT)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(bookDto, actual, "id"));
    }

    @Test
    @DisplayName("Test getAll endpoint for book")
    @WithMockUser(username = USER)
    void getAll_validBooks_returnResponse() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(2L);
        bookDto.setTitle("Test Title2");
        bookDto.setAuthor("Test Author2");
        bookDto.setCategoryIds(Set.of(2L));
        bookDto.setIsbn("978-0-14-103614-4");
        bookDto.setPrice(BigDecimal.valueOf(19.99));
        bookDto.setDescription("Test Description2");
        bookDto.setCoverImage("Test Cover Image2");
        List<BookDto> expected = List.of(BOOK_DTO, bookDto);

        MvcResult result = mockMvc.perform(
                        get(BOOK_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> actual = List.of(objectMapper.readValue(result
                .getResponse().getContentAsString(), BookDto[].class));

        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test get endpoint for books")
    @WithMockUser(username = USER)
    void getById_validBook_returnResponse() throws Exception {
        MvcResult result = mockMvc.perform(
                        get(BOOK_ENDPOINT + ID_FOR_ENDPOINTS)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(BOOK_DTO, actual, "id"));
    }

    @Test
    @WithMockUser(username = USER, roles = {USER})
    @DisplayName("Delete book by valid id")
    void delete_validId_successful() throws Exception {
        mockMvc.perform(delete(BOOK_ENDPOINT + ID_FOR_ENDPOINTS))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = USER, roles = {USER})
    @DisplayName("Update book by id")
    void update_validRequest_returnsExpectedBook() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(BOOK_ENDPOINT + ID_FOR_ENDPOINTS)
                        .content(objectMapper.writeValueAsString(CREATE_BOOK_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(BOOK_DTO, actual, "id"));
    }

    @Test
    @DisplayName("Test getAll books by params")
    @WithMockUser(username = USER)
    public void getAllBooks_validParams_returnsBooksList() throws Exception {
        List<BookDto> expected = List.of(BOOK_DTO);
        MvcResult mvcResult = mockMvc.perform(get(BOOK_ENDPOINT
                        + "/search?author=" + BOOK_DTO.getAuthor()))
                .andReturn();
        List<BookDto> actual = List.of(objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto[].class));

        assertEquals(1, actual.size());
        assertEquals(expected, actual);
    }
}
