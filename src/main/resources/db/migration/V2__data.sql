INSERT INTO products (id, name, description, price, delivery_price)
VALUES ('8F2E9176-35EE-4F0A-AE55-83023D2DB1A3', 'Samsung Galaxy S7', 'Newest mobile product from Samsung.', 1024.99,
        16.99);

INSERT INTO products(id, name, description, price, delivery_price)
VALUES ('DE1287C0-4B15-4A7B-9D8A-DD21B3CAFEC3', 'Apple iPhone 6S', 'Newest mobile product from Apple.', 1299.99, 15.99);

INSERT INTO product_options(id, product_id, name, description)
VALUES ('0643CCF0-AB00-4862-B3C5-40E2731ABCC9', '8F2E9176-35EE-4F0A-AE55-83023D2DB1A3', 'White',
        'White Samsung Galaxy S7');
INSERT INTO product_options(id, product_id, name, description)
VALUES ('A21D5777-A655-4020-B431-624BB331E9A2', '8F2E9176-35EE-4F0A-AE55-83023D2DB1A3', 'Black',
        'Black Samsung Galaxy S7');
INSERT INTO product_options(id, product_id, name, description)
VALUES ('5C2996AB-54AD-4999-92D2-89245682D534', 'DE1287C0-4B15-4A7B-9D8A-DD21B3CAFEC3', 'Rose Gold',
        'Gold Apple iPhone 6S');
INSERT INTO product_options(id, product_id, name, description)
VALUES ('9AE6F477-A010-4EC9-B6A8-92A85D6C5F03', 'DE1287C0-4B15-4A7B-9D8A-DD21B3CAFEC3', 'White',
        'White Apple iPhone 6S');
INSERT INTO product_options(id, product_id, name, description)
VALUES ('4E2BC5F2-699A-4C42-802E-CE4B4D2AC0EF', 'DE1287C0-4B15-4A7B-9D8A-DD21B3CAFEC3', 'Black',
        'Black Apple iPhone 6S');


-- Data for Spring Security
-- password: password1
INSERT INTO users (id, username, password)
VALUES (1, 'admin', '$2a$04$Ye7/lJoJin6.m9sOJZ9ujeTgHEVM4VXgI2Ingpsnf9gXyXEXf/IlW')
ON CONFLICT (id) DO NOTHING;
-- password: password2
INSERT INTO users (id, username, password)
VALUES (2, 'user1', '$2a$04$StghL1FYVyZLdi8/DIkAF./2rz61uiYPI3.MaAph5hUq03XKeflyW')
ON CONFLICT (id) DO NOTHING;
-- password: password3
INSERT INTO users (id, username, password)
VALUES (3, 'user2', '$2a$04$Lk4zqXHrHd82w5/tiMy8ru9RpAXhvFfmHOuqTmFPWQcUhBD8SSJ6W')
ON CONFLICT (id) DO NOTHING;

INSERT INTO roles (id, description, name)
VALUES (4, 'Admin role', 'ADMIN')
ON CONFLICT (id) DO NOTHING;

INSERT INTO roles (id, description, name)
VALUES (5, 'User role', 'USER')
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 4)
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES (2, 5)
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES (3, 5)
ON CONFLICT (user_id, role_id) DO NOTHING;
