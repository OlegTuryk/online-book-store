package com.app.onlinebookstore;

import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.service.BookService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class OnlineBookStoreApplication {
    private final BookService bookService;

    public static void main(String[] args) {

        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book onePiece = new Book();
            onePiece.setAuthor("Eichiro Oda");
            onePiece.setTitle("One piece");
            onePiece.setIsbn("12335423");
            onePiece.setPrice(BigDecimal.valueOf(1000));
            bookService.save(onePiece);
            System.out.println(bookService.findAll());
        };
    }
}
