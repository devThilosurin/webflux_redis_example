DROP TABLE IF EXISTS product;
CREATE TABLE product (
    id serial PRIMARY KEY,
    description VARCHAR(500),
    price DECIMAL(10,2) NOT NULL
);