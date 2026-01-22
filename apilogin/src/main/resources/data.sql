-- ADMIN
INSERT INTO usuarios
(username, password, email, nombre, activado, codigo_activacion, fecha_expiracion_codigo, rol, two_factor_enabled, two_factor_secret)
VALUES
    (
        'admin',
        '$2b$12$nizRYTzHohjIiAnpGCeNYOhZPTgUZ9fjvRl8YfzmUhm6rYlof6jxa', -- admin123
        'admin@example.com',
        'Administrador',
        true,
        NULL,
        NULL,
        'ADMIN',
        false,
        ''
    );

-- JUAN
INSERT INTO usuarios
(username, password, email, nombre, activado, codigo_activacion, fecha_expiracion_codigo, rol, two_factor_enabled, two_factor_secret)
VALUES
    (
        'juan',
        '$2b$12$AXFHPY8UifAuy78WZo5JxunQgoekGv31lg4x2yMatfBf55yGEsWAS', -- juan123
        'juan@example.com',
        'Juan Pérez',
        true,
        NULL,
        NULL,
        'USER',
        false,
        ''
    );

-- MARIA
INSERT INTO usuarios
(username, password, email, nombre, activado, codigo_activacion, fecha_expiracion_codigo, rol, two_factor_enabled, two_factor_secret)
VALUES
    (
        'maria',
        '$2b$12$5JXFGFPwhPzAwfD7jMr9keeVIPpOk5Oro.T566AL1VOuy9LVMh4dS', -- maria123
        'maria@example.com',
        'María García',
        true,
        NULL,
        NULL,
        'USER',
        false,
        ''
    );

-- Renos de prueba
INSERT INTO renos (user_id, nombre, color, cuernos) VALUES
                                                        (2, 'Rodolfo', 'Rojo', true),
                                                        (2, 'Antonio', 'Verde', false),
                                                        (3, 'Cornudo', 'Azul', true);

-- Tabla para tokens JWT (sistema de revocación)
CREATE TABLE IF NOT EXISTS tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(500) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    username VARCHAR(100) NOT NULL,
    token_type VARCHAR(20) NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_token ON tokens(token);
CREATE INDEX IF NOT EXISTS idx_user_id ON tokens(user_id);
CREATE INDEX IF NOT EXISTS idx_expires_at ON tokens(expires_at);

