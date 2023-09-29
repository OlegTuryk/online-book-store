package com.app.onlinebookstore.controller;

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
import org.junit.jupiter.api.Assertions;
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
    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    @DisplayName("Create a new category")
    void createCategory_validRequestDto_returnCategoryDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(CREATE_CATEGORY_REQUEST_DTO);

        MvcResult result = mockMvc.perform(
                post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(CATEGORY_DTO, actual, "id"));
    }

    @Test
    @DisplayName("Test getAll endpoint for category")
    @WithMockUser(username = "admin")
    void getAll_validCategories_returnResponse() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(2L);
        categoryDto.setName("Test Category 2");
        categoryDto.setDescription("Description for Test Category 2");
        List<CategoryDto> expected = List.of(CATEGORY_DTO, categoryDto);

        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<CategoryDto> actual = List.of(objectMapper.readValue(result
                .getResponse().getContentAsString(), CategoryDto[].class));
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test get endpoint for category")
    @WithMockUser(username = "admin")
    void getById_validCategory_returnResponse() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/categories/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(CATEGORY_DTO, actual, "id"));
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    @DisplayName("Delete category by valid id")
    void delete_validId_successful() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    @DisplayName("Update category by id")
    void update_validRequest_returnsExpectedCategory() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/categories/1")
                        .content(objectMapper.writeValueAsString(CREATE_CATEGORY_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(CATEGORY_DTO, actual, "id"));
    }

    @WithMockUser(username = "ADMIN")
    @Test
    @DisplayName("Get book by valid category id")
    void getBooksByCategoryId_validId_returnListOfBookDtoWithoutCategoryId() throws Exception {

        List<BookDtoWithoutCategoryIds> expected = List.of(BOOK_DTO_WITHOUT_CATEGORY_IDS);
        MvcResult mvcResult = mockMvc.perform(get("/categories/1/books"))
                .andReturn();

        List<BookDtoWithoutCategoryIds> actual = List.of(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class));

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(expected, actual);
    }
}
