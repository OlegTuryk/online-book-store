INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (2, 'user@example.com', '$2a$12$YidPrKXOD3Bb6wcgh8WcNeXX8D4MLhR.bPZ0/0scXIoBQMTrLZRmi', 'FirstName', 'LastName', 'Shipping Address', false);

INSERT INTO user_role (user_id, role_id)
VALUES (2, 2);