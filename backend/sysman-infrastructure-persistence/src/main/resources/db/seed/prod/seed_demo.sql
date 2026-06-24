-- Datos de ejemplo para demo en produccion (Oracle ADB).
-- Ejecutar manualmente en el SQL Worksheet, conectado como el usuario sysman.
-- No forma parte de las migraciones Flyway (no se ejecuta automaticamente).

INSERT INTO cliente (numero_identificacion, nombre, direccion, telefono, email, creado_por)
VALUES ('1000000001', 'Juan Perez', 'Calle 10 # 20-30, Bogota', '3001234567', 'juan.perez@example.com', 'seed');

INSERT INTO cliente (numero_identificacion, nombre, direccion, telefono, email, creado_por)
VALUES ('1000000002', 'Maria Gomez', 'Av. Siempre Viva 742, Medellin', '3007654321', 'maria.gomez@example.com', 'seed');

INSERT INTO cliente (numero_identificacion, nombre, direccion, telefono, email, creado_por)
VALUES ('1000000003', 'Carlos Ramirez', 'Cra 45 # 12-08, Cali', '3019876543', 'carlos.ramirez@example.com', 'seed');

INSERT INTO cliente (numero_identificacion, nombre, direccion, telefono, email, creado_por)
VALUES ('1000000004', 'Laura Torres', 'Calle 80 # 9-21, Barranquilla', '3026549870', 'laura.torres@example.com', 'seed');

INSERT INTO cliente (numero_identificacion, nombre, direccion, telefono, email, creado_por)
VALUES ('1000000005', 'Andres Lopez', 'Av. El Dorado # 68-55, Bogota', '3033456789', 'andres.lopez@example.com', 'seed');

-- Orden 1: CREADA
INSERT INTO orden (cliente_id, tipo_orden, estado, descripcion, direccion_servicio, creado_por)
VALUES (1, 'INSTALACION', 'CREADA', 'Instalacion de medidor nuevo', 'Calle 10 # 20-30, Bogota', 'mfontana');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (1, NULL, 'CREADA', 'mfontana', 'Creacion inicial de la orden');

-- Orden 2: ASIGNADA
INSERT INTO orden (cliente_id, tipo_orden, estado, descripcion, direccion_servicio, creado_por)
VALUES (2, 'MANTENIMIENTO', 'ASIGNADA', 'Mantenimiento preventivo de red', 'Av. Siempre Viva 742, Medellin', 'jrincon');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (2, NULL, 'CREADA', 'jrincon', 'Creacion inicial de la orden');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (2, 'CREADA', 'ASIGNADA', 'jrincon', 'Asignada a tecnico de campo');
UPDATE orden SET estado = 'ASIGNADA', version = 1, modificado_por = 'jrincon', fecha_modificacion = SYSTIMESTAMP WHERE cliente_id = 2 AND tipo_orden = 'MANTENIMIENTO';

-- Orden 3: EN_PROCESO
INSERT INTO orden (cliente_id, tipo_orden, estado, descripcion, direccion_servicio, creado_por)
VALUES (3, 'REPARACION_EMERGENCIA', 'EN_PROCESO', 'Falla en transformador de zona', 'Cra 45 # 12-08, Cali', 'dcastro');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (3, NULL, 'CREADA', 'dcastro', 'Creacion inicial de la orden');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (3, 'CREADA', 'ASIGNADA', 'dcastro', 'Asignada a brigada de emergencia');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (3, 'ASIGNADA', 'EN_PROCESO', 'dcastro', 'Tecnico en sitio, iniciando reparacion');
UPDATE orden SET estado = 'EN_PROCESO', version = 2, modificado_por = 'dcastro', fecha_modificacion = SYSTIMESTAMP WHERE cliente_id = 3 AND tipo_orden = 'REPARACION_EMERGENCIA';

-- Orden 4: COMPLETADA
INSERT INTO orden (cliente_id, tipo_orden, estado, descripcion, direccion_servicio, creado_por)
VALUES (4, 'RECONEXION', 'COMPLETADA', 'Reconexion de servicio tras pago', 'Calle 80 # 9-21, Barranquilla', 'mfontana');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (4, NULL, 'CREADA', 'mfontana', 'Creacion inicial de la orden');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (4, 'CREADA', 'ASIGNADA', 'mfontana', 'Asignada a tecnico de campo');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (4, 'ASIGNADA', 'EN_PROCESO', 'mfontana', 'Tecnico en sitio');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (4, 'EN_PROCESO', 'COMPLETADA', 'mfontana', 'Servicio reconectado exitosamente');
UPDATE orden SET estado = 'COMPLETADA', version = 3, modificado_por = 'mfontana', fecha_modificacion = SYSTIMESTAMP WHERE cliente_id = 4 AND tipo_orden = 'RECONEXION';

-- Orden 5: CANCELADA
INSERT INTO orden (cliente_id, tipo_orden, estado, descripcion, direccion_servicio, creado_por)
VALUES (5, 'INSPECCION', 'CANCELADA', 'Inspeccion de seguridad solicitada por el cliente', 'Av. El Dorado # 68-55, Bogota', 'jrincon');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (5, NULL, 'CREADA', 'jrincon', 'Creacion inicial de la orden');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (5, 'CREADA', 'CANCELADA', 'jrincon', 'Cliente cancelo la solicitud');
UPDATE orden SET estado = 'CANCELADA', version = 1, modificado_por = 'jrincon', fecha_modificacion = SYSTIMESTAMP WHERE cliente_id = 5 AND tipo_orden = 'INSPECCION';

-- Orden 6: RECHAZADA
INSERT INTO orden (cliente_id, tipo_orden, estado, descripcion, direccion_servicio, creado_por)
VALUES (1, 'CORTE', 'RECHAZADA', 'Corte de servicio por mora', 'Calle 10 # 20-30, Bogota', 'dcastro');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (6, NULL, 'CREADA', 'dcastro', 'Creacion inicial de la orden');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (6, 'CREADA', 'RECHAZADA', 'dcastro', 'Cliente realizo el pago antes del corte');
UPDATE orden SET estado = 'RECHAZADA', version = 1, modificado_por = 'dcastro', fecha_modificacion = SYSTIMESTAMP WHERE cliente_id = 1 AND tipo_orden = 'CORTE';

-- Orden 7: CREADA (segunda orden para el cliente 2)
INSERT INTO orden (cliente_id, tipo_orden, estado, descripcion, direccion_servicio, creado_por)
VALUES (2, 'INSPECCION', 'CREADA', 'Inspeccion rutinaria anual', 'Av. Siempre Viva 742, Medellin', 'lvargas');
INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo)
VALUES (7, NULL, 'CREADA', 'lvargas', 'Creacion inicial de la orden');

COMMIT;
