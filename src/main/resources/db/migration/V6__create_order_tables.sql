-- Criação da tabela de pedidos
CREATE TABLE customer_order (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(150) NOT NULL,
    customer_phone VARCHAR(30),
    delivery_notes TEXT,
    store_location VARCHAR(50) NOT NULL DEFAULT 'matriz',
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'COMPLETED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT,
    CONSTRAINT fk_customer_order_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE SET NULL
);

-- Criação da tabela de itens de pedido
CREATE TABLE order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    category_name VARCHAR(100),
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES customer_order(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT
);

-- Índices para otimizar consultas analíticas
CREATE INDEX idx_customer_order_created_at ON customer_order(created_at);
CREATE INDEX idx_customer_order_store ON customer_order(store_location);
CREATE INDEX idx_order_item_order_id ON order_item(order_id);
CREATE INDEX idx_order_item_product_id ON order_item(product_id);

-- Inserção de pedidos iniciais para calibragem do Analytics
INSERT INTO customer_order (id, customer_name, customer_phone, delivery_notes, store_location, total_amount, status, created_at)
VALUES 
(1, 'Maria do Carmo Santos', '91981112233', 'Entregar na Paróquia de Nazaré', 'matriz', 104.70, 'COMPLETED', NOW() - INTERVAL '2 days'),
(2, 'Antônio Carlos Silva', '91982223344', 'Retirada no balcão', 'filial', 75.80, 'COMPLETED', NOW() - INTERVAL '1 day'),
(3, 'Francisca das Chagas', '91983334455', 'Embalar para presente de Círio', 'matriz', 142.50, 'COMPLETED', NOW() - INTERVAL '5 hours'),
(4, 'José Ribamar Pereira', '91984445566', 'Urgente para novena', 'matriz', 56.70, 'COMPLETED', NOW() - INTERVAL '1 hour'),
(5, 'Cláudia Regina Barros', '91985556677', 'Retirar no Shopping Pátio Belém', 'filial', 89.90, 'COMPLETED', NOW() - INTERVAL '30 minutes');

SELECT setval('customer_order_id_seq', (SELECT COALESCE(MAX(id), 1) FROM customer_order));

INSERT INTO order_item (order_id, product_id, product_name, category_name, quantity, unit_price, total_price)
VALUES
(1, 1, 'Vela 7 dias Branca', 'Classicas', 3, 10.00, 30.00),
(1, 2, 'Vela Lavanda', 'Aromaticas', 2, 20.00, 40.00),
(1, 3, 'Porta Velas', 'Decoracao', 1, 34.70, 34.70),
(2, 2, 'Vela Lavanda', 'Aromaticas', 2, 20.00, 40.00),
(2, 3, 'Porta Velas', 'Decoracao', 1, 35.80, 35.80),
(3, 1, 'Vela 7 dias Branca', 'Classicas', 5, 10.00, 50.00),
(3, 2, 'Vela Lavanda', 'Aromaticas', 3, 20.00, 60.00),
(3, 3, 'Porta Velas', 'Decoracao', 1, 32.50, 32.50),
(4, 1, 'Vela 7 dias Branca', 'Classicas', 3, 10.00, 30.00),
(4, 2, 'Vela Lavanda', 'Aromaticas', 1, 20.00, 20.00),
(4, 3, 'Porta Velas', 'Decoracao', 1, 6.70, 6.70),
(5, 2, 'Vela Lavanda', 'Aromaticas', 3, 20.00, 60.00),
(5, 3, 'Porta Velas', 'Decoracao', 1, 29.90, 29.90);