package com.app.onlinebookstore.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookSearchParametersDto {
    private String title;
    private String author;
    private String isbn;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
