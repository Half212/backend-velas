CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    action VARCHAR(255) NOT NULL,
    entity VARCHAR(100),
    entity_id BIGINT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);

-- Inserir cargos
INSERT INTO role (id, name) VALUES (1, 'ROLE_SUPERVISOR'), (2, 'ROLE_ADMIN'), (3, 'ROLE_COMMON');

-- Ajustar a sequência após insert manual para evitar erro de conflito de ID
SELECT setval('role_id_seq', (SELECT MAX(id) FROM role));

-- Inserir usuário supervisor (senha: admin123, hash bcrypt: $2a$10$EblZqNptyYvcLm/VwDCVAuBjzPKnV702p.W1.d./Zz09z33Zf4JpG)
INSERT INTO app_user (name, email, phone, password, role_id) 
VALUES ('Supervisor Geral', 'supervisor@velas.com', '00000000000', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzPKnV702p.W1.d./Zz09z33Zf4JpG', 1);
