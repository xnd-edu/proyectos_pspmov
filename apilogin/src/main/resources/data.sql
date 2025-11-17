INSERT INTO usuarios (username, password, email, nombre, rol) VALUES
                                                                  ('admin', '$2b$12$nizRYTzHohjIiAnpGCeNYOhZPTgUZ9fjvRl8YfzmUhm6rYlof6jxa', 'admin@example.com', 'Administrador', 'ADMIN'),
                                                                  ('juan', '$2b$12$AXFHPY8UifAuy78WZo5JxunQgoekGv31lg4x2yMatfBf55yGEsWAS', 'juan@example.com', 'Juan Pérez', 'USER'),
                                                                  ('maria', '$2b$12$5JXFGFPwhPzAwfD7jMr9keeVIPpOk5Oro.T566AL1VOuy9LVMh4dS', 'maria@example.com', 'María García', 'USER');

INSERT INTO renos (user_id, nombre, color, cuernos) VALUES
                                                        (2, 'Rodolfo', 'Rojo', true),
                                                        (2, 'Antonio', 'Verde', false),
                                                        (3, 'Cornudo', 'Azul', true);
