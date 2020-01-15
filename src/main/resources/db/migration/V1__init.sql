CREATE EXTENSION pg_trgm;

CREATE TABLE IF NOT EXISTS products (
    id UUID NOT NULL,
    name varchar(17) NOT NULL,
    description varchar(35) DEFAULT NULL,
    price decimal(6, 2) default 0.00,
    delivery_price decimal(4,2) default 0.00,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),

    PRIMARY KEY (id)
);

CREATE INDEX idx_products_lower_name ON products USING GIN (name gin_trgm_ops);

CREATE TABLE IF NOT EXISTS product_options (
    id UUID NOT NULL,
    product_id UUID NOT NULL,
    name varchar(9) NOT NULL,
    description varchar(23) DEFAULT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),

    PRIMARY KEY (id)
);

ALTER TABLE product_options
  ADD CONSTRAINT fk_product_options_to_product FOREIGN KEY (product_id) REFERENCES products;

CREATE INDEX idx_product_options_fk_index ON product_options (product_id);

-- These tables are required for Spring Security
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL NOT NULL,
    description varchar(255),
    name varchar(255),
    primary key (id)
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL NOT NULL,
    password varchar(255),
    username varchar(255),
    primary key (id)
);

CREATE INDEX idx_user_username ON users(username);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id integer not null,
    role_id integer not null,
    primary key (user_id, role_id)
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id);
