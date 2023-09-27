INSERT INTO categories (id, name) VALUES (1, 'Test Category');

INSERT INTO books (id, title, author, isbn, price, description, cover_image) VALUES (1, 'Test Title', 'Test Author', '1234567890123', 19.99, 'Test Description', 'Test Cover Image');

INSERT INTO book_category (book_id, category_id) VALUES (1, 1);
