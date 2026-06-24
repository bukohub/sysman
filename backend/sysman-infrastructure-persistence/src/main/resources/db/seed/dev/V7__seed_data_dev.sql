-- Datos de prueba SOLO para perfil dev. Esta ubicacion (db/seed/dev) se agrega a
-- spring.flyway.locations unicamente en application-dev.yml, nunca en produccion.

INSERT INTO cliente (numero_identificacion, nombre, direccion, telefono, email, creado_por)
VALUES ('1000000001', 'Juan Perez', 'Calle 10 # 20-30, Bogota', '3001234567', 'juan.perez@example.com', 'seed');

INSERT INTO cliente (numero_identificacion, nombre, direccion, telefono, email, creado_por)
VALUES ('1000000002', 'Maria Gomez', 'Av. Siempre Viva 742, Medellin', '3007654321', 'maria.gomez@example.com', 'seed');

INSERT INTO orden (cliente_id, tipo_orden, estado, descripcion, direccion_servicio, creado_por)
VALUES (1, 'INSTALACION', 'CREADA', 'Instalacion de medidor nuevo', 'Calle 10 # 20-30, Bogota', 'seed');

INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (1, NULL, 'CREADA', 'seed', 'Creacion inicial de la orden');

INSERT INTO orden (cliente_id, tipo_orden, estado, descripcion, direccion_servicio, creado_por)
VALUES (2, 'MANTENIMIENTO', 'ASIGNADA', 'Mantenimiento preventivo de red', 'Av. Siempre Viva 742, Medellin', 'seed');

INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (2, NULL, 'CREADA', 'seed', 'Creacion inicial de la orden');

INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (2, 'CREADA', 'ASIGNADA', 'seed', 'Asignada a tecnico de campo');

UPDATE orden SET estado = 'ASIGNADA', version = 1 WHERE id = 2;

COMMIT;
