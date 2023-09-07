package com.app.onlinebookstore.repository;

import com.app.onlinebookstore.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    public Optional<Book> getBookById(Long id);
}
