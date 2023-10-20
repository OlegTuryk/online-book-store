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

import com.app.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.app.onlinebookstore.dto.category.CategoryDto;
import com.app.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
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
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final CreateCategoryRequestDto CREATE_CATEGORY_REQUEST_DTO
            = new CreateCategoryRequestDto();
    private static final CategoryDto CATEGORY_DTO = new CategoryDto();
    private static final BookDtoWithoutCategoryIds BOOK_DTO_WITHOUT_CATEGORY_IDS
            = new BookDtoWithoutCategoryIds();
    private static final String CATEGORY_ENDPOINT = "/categories";
    private static final String ID_FOR_ENDPOINTS = "/1";
    private static final String ID_TEXT = "id";
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

        CREATE_CATEGORY_REQUEST_DTO.setName("Test Category 1");
        CREATE_CATEGORY_REQUEST_DTO.setDescription("Description for Test Category 1");

        CATEGORY_DTO.setId(1L);
        CATEGORY_DTO.setName(CREATE_CATEGORY_REQUEST_DTO.getName());
        CATEGORY_DTO.setDescription(CREATE_CATEGORY_REQUEST_DTO.getDescription());

        BOOK_DTO_WITHOUT_CATEGORY_IDS.setId(1L);
        BOOK_DTO_WITHOUT_CATEGORY_IDS.setTitle("Test Title1");
        BOOK_DTO_WITHOUT_CATEGORY_IDS.setAuthor("Test Author1");
        BOOK_DTO_WITHOUT_CATEGORY_IDS.setIsbn("978-3-16-148410-0");
        BOOK_DTO_WITHOUT_CATEGORY_IDS.setPrice(BigDecimal.valueOf(19.99));
        BOOK_DTO_WITHOUT_CATEGORY_IDS.setDescription("Test Description1");
        BOOK_DTO_WITHOUT_CATEGORY_IDS.setCoverImage("Test Cover Image1");
    }

    @Test
    @WithMockUser(username = USER, roles = {USER})
    @DisplayName("Create a new category")
    void createCategory_validRequestDto_returnCategoryDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(CREATE_CATEGORY_REQUEST_DTO);
        MvcResult result = mockMvc.perform(
                post(CATEGORY_ENDPOINT)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(CATEGORY_DTO, actual, ID_TEXT));
    }

    @Test
    @DisplayName("Test getAll endpoint for category")
    @WithMockUser(username = USER)
    void getAll_validCategories_returnResponse() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(2L);
        categoryDto.setName("Test Category 2");
        categoryDto.setDescription("Description for Test Category 2");
        List<CategoryDto> expected = List.of(CATEGORY_DTO, categoryDto);

        MvcResult result = mockMvc.perform(
                        get(CATEGORY_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<CategoryDto> actual = List.of(objectMapper.readValue(result
                .getResponse().getContentAsString(), CategoryDto[].class));

        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test get endpoint for category")
    @WithMockUser(username = USER)
    void getById_validCategory_returnResponse() throws Exception {
        MvcResult result = mockMvc.perform(
                        get(CATEGORY_ENDPOINT + ID_FOR_ENDPOINTS)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(CATEGORY_DTO, actual, ID_TEXT));
    }

    @Test
    @WithMockUser(username = USER, roles = {USER})
    @DisplayName("Delete category by valid id")
    void delete_validId_successful() throws Exception {
        mockMvc.perform(delete(CATEGORY_ENDPOINT + ID_FOR_ENDPOINTS))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = USER, roles = {USER})
    @DisplayName("Update category by id")
    void update_validRequest_returnsExpectedCategory() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(CATEGORY_ENDPOINT + ID_FOR_ENDPOINTS)
                        .content(objectMapper.writeValueAsString(CREATE_CATEGORY_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(CATEGORY_DTO, actual, ID_TEXT));
    }

    @WithMockUser(username = USER)
    @Test
    @DisplayName("Get book by valid category id")
    void getBooksByCategoryId_validId_returnListOfBookDtoWithoutCategoryId() throws Exception {
        List<BookDtoWithoutCategoryIds> expected = List.of(BOOK_DTO_WITHOUT_CATEGORY_IDS);
        MvcResult mvcResult = mockMvc.perform(get(CATEGORY_ENDPOINT + ID_FOR_ENDPOINTS + "/books"))
                .andReturn();

        List<BookDtoWithoutCategoryIds> actual = List.of(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class));

        assertEquals(1, actual.size());
        assertEquals(expected, actual);
    }
}
