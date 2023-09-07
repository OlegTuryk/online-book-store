package com.app.onlinebookstore;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class OnlineBookStoreApplication {
    public static void main(String[] args) {

        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }
}
