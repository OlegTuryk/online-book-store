INSERT INTO categories (id, name, description) VALUES (1, 'Test Category 1', 'Description for Test Category 1');

INSERT INTO categories (id, name, description) VALUES (2, 'Test Category 2', 'Description for Test Category 2');

INSERT INTO books (id, title, author, isbn, price, description, cover_image) VALUES (1, 'Test Title1', 'Test Author1', '978-3-16-148410-0', 19.99, 'Test Description1', 'Test Cover Image1');

INSERT INTO books (id, title, author, isbn, price, description, cover_image) VALUES (2, 'Test Title2', 'Test Author2', '978-0-14-103614-4', 19.99, 'Test Description2', 'Test Cover Image2');

INSERT INTO book_category (book_id, category_id) VALUES (1, 1);

INSERT INTO book_category (book_id, category_id) VALUES (2, 2);
