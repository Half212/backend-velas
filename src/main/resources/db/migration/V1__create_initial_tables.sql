CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    category_id BIGINT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(id)
);

-- Inserção de dados iniciais para teste dos juniores
INSERT INTO category (name) VALUES ('Classicas'), ('Aromaticas'), ('Decoração');
INSERT INTO product (name, description, price, stock_quantity, category_id) 
VALUES ('Vela 7 dias Branca', 'Vela 7 duas', 10.00, 50, 1),
       ('Vela Lavanda', 'Perfumada de Lavanda', 20.00, 50, 2),
       ('Porta Velas', 'Porta Velas de Vidro', 35.00, 50, 3);