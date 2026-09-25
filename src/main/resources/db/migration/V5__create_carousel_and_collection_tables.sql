CREATE TABLE carousel_slide (
    id VARCHAR(64) PRIMARY KEY,
    image TEXT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    button_text VARCHAR(100),
    button_link VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    order_index INT NOT NULL DEFAULT 1
);

INSERT INTO carousel_slide (id, image, title, description, button_text, button_link, active, order_index) VALUES
('slide-1', '/images/velaartesanal.jpeg', 'Velas Artesanais de Cera', 'Desde 1922 iluminando lares, altares e momentos de profunda devoção.', 'Explorar Coleção', '/loja', true, 1),
('slide-2', '/images/velaartesanal2.jpeg', 'Velas Religiosas de Devoção', 'Fé, oração e tradição moldadas à mão com o mais puro artesanato.', 'Ver Velas Religiosas', '/loja', true, 2),
('slide-3', '/images/velaartesanal3.jpeg', 'Velas Decorativas Aromáticas', 'Ambientes acolhedores com aromas envolventes e luz serena.', 'Conhecer Linha Aromática', '/loja', true, 3);

CREATE TABLE home_collection (
    id VARCHAR(64) PRIMARY KEY,
    tag VARCHAR(100),
    title VARCHAR(255) NOT NULL,
    subtitle TEXT,
    image TEXT NOT NULL,
    button_text VARCHAR(100),
    link VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    order_index INT NOT NULL DEFAULT 1
);

INSERT INTO home_collection (id, tag, title, subtitle, image, button_text, link, active, order_index) VALUES
('collection-1', 'Tradição & Fé', 'Velas Religiosas', 'Fé e devoção moldadas à mão para os seus momentos sagrados.', '/images/velareligiosa.png', 'Ver coleção', '/loja', true, 1),
('collection-2', 'Sofisticação', 'Velas Decorativas', 'Ambientes mais acolhedores, elegantes e iluminados.', '/images/veladecorativa2.png', 'Ver coleção', '/loja', true, 2),
('collection-3', 'Sensações', 'Velas Aromáticas', 'Aromas envolventes que transformam sua casa e bem-estar.', '/images/velaaromatica.jpeg', 'Ver coleção', '/loja', true, 3),
('collection-4', 'Artesanal', 'Sebo de Holanda', 'Tradição centenária e pureza para seu cuidado diário.', '/images/sebodeholanda.jpeg', 'Ver coleção', '/loja', true, 4);

CREATE TABLE home_collection_header (
    id BIGINT PRIMARY KEY,
    tag VARCHAR(100),
    title VARCHAR(255) NOT NULL
);

INSERT INTO home_collection_header (id, tag, title) VALUES
(1, 'Artesanato & Fé', 'Nossas Coleções');