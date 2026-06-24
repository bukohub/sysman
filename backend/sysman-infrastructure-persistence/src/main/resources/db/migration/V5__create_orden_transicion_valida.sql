-- Matriz de transiciones validas como datos (no logica hardcodeada): permite ajustar
-- reglas de negocio sin recompilar el paquete PL/SQL. Espejo de
-- com.sysman.orden.domain.service.ValidadorTransicionEstado en el backend (defensa en
-- profundidad: se valida en Java antes de invocar el procedimiento, y otra vez aqui dentro
-- de la transaccion de base de datos).

CREATE TABLE orden_transicion_valida (
    estado_origen   VARCHAR2(20) NOT NULL,
    estado_destino  VARCHAR2(20) NOT NULL,
    CONSTRAINT pk_orden_transicion_valida PRIMARY KEY (estado_origen, estado_destino)
);

INSERT INTO orden_transicion_valida (estado_origen, estado_destino) VALUES ('CREADA', 'ASIGNADA');
INSERT INTO orden_transicion_valida (estado_origen, estado_destino) VALUES ('CREADA', 'CANCELADA');
INSERT INTO orden_transicion_valida (estado_origen, estado_destino) VALUES ('CREADA', 'RECHAZADA');
INSERT INTO orden_transicion_valida (estado_origen, estado_destino) VALUES ('ASIGNADA', 'EN_PROCESO');
INSERT INTO orden_transicion_valida (estado_origen, estado_destino) VALUES ('ASIGNADA', 'CANCELADA');
INSERT INTO orden_transicion_valida (estado_origen, estado_destino) VALUES ('EN_PROCESO', 'COMPLETADA');
INSERT INTO orden_transicion_valida (estado_origen, estado_destino) VALUES ('EN_PROCESO', 'CANCELADA');
INSERT INTO orden_transicion_valida (estado_origen, estado_destino) VALUES ('RECHAZADA', 'CREADA');

COMMIT;
