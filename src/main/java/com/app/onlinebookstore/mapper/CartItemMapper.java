package com.app.onlinebookstore.mapper;

import com.app.onlinebookstore.config.MapperConfig;
import com.app.onlinebookstore.dto.shopping_cart.CartItemDto;
import com.app.onlinebookstore.dto.shopping_cart.CreateCartItemDto;
import com.app.onlinebookstore.dto.shopping_cart.UpdateCartItemDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.model.CartItem;
import com.app.onlinebookstore.model.ShoppingCart;
import com.app.onlinebookstore.repository.BookRepository;
import com.app.onlinebookstore.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {

    @Mapping(target = "bookId", source = "cartItem.book.id")
    @Mapping(target = "bookTitle", source = "cartItem.book.title")
    CartItemDto toDto(CartItem cartItem);

}
