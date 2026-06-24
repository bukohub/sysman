CREATE TABLE orden_historico (
    id                  NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    orden_id            NUMBER        NOT NULL,
    estado_anterior     VARCHAR2(20),
    estado_nuevo        VARCHAR2(20)  NOT NULL,
    fecha_cambio        TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    usuario             VARCHAR2(100) NOT NULL,
    motivo              VARCHAR2(500),
    CONSTRAINT fk_historico_orden FOREIGN KEY (orden_id) REFERENCES orden(id)
)
PARTITION BY RANGE (fecha_cambio)
INTERVAL (NUMTOYMINTERVAL(1,'MONTH'))
(
    PARTITION p_inicial VALUES LESS THAN (TIMESTAMP '2026-01-01 00:00:00')
);

COMMENT ON TABLE orden_historico IS 'Historico de cambios de estado de cada orden - fuente de verdad de auditoria de negocio.';
