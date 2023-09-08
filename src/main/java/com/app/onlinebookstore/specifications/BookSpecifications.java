package com.app.onlinebookstore.specifications;

import com.app.onlinebookstore.dto.BookSearchParametersDto;
import com.app.onlinebookstore.model.Book;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecifications {
    public static Specification<Book> searchBooks(BookSearchParametersDto searchParameters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (searchParameters.getTitle() != null) {
                predicates.add(criteriaBuilder.equal(root.get("title"), searchParameters.getTitle()));
            }
            if (searchParameters.getAuthor() != null) {
                predicates.add(criteriaBuilder.equal(root.get("author"), searchParameters.getAuthor()));
            }
            if (searchParameters.getIsbn() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isbn"), searchParameters.getIsbn()));
            }
            if (searchParameters.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"), searchParameters.getMinPrice()));
            }
            if (searchParameters.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"), searchParameters.getMaxPrice()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
