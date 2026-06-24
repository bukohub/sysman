CREATE TABLE orden (
    id                  NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cliente_id          NUMBER        NOT NULL,
    tipo_orden          VARCHAR2(30)  NOT NULL,
    estado              VARCHAR2(20)  NOT NULL,
    descripcion         VARCHAR2(1000),
    direccion_servicio  VARCHAR2(300),
    creado_por          VARCHAR2(100) NOT NULL,
    fecha_creacion      TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    modificado_por      VARCHAR2(100),
    fecha_modificacion  TIMESTAMP,
    version             NUMBER        DEFAULT 0 NOT NULL,
    CONSTRAINT fk_orden_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT chk_orden_estado CHECK (estado IN
        ('CREADA','ASIGNADA','EN_PROCESO','COMPLETADA','CANCELADA','RECHAZADA')),
    CONSTRAINT chk_orden_tipo CHECK (tipo_orden IN
        ('INSTALACION','MANTENIMIENTO','CORTE','RECONEXION','INSPECCION','REPARACION_EMERGENCIA'))
)
PARTITION BY RANGE (fecha_creacion)
INTERVAL (NUMTOYMINTERVAL(1,'MONTH'))
(
    PARTITION p_inicial VALUES LESS THAN (TIMESTAMP '2026-01-01 00:00:00')
);

COMMENT ON TABLE orden IS 'Ordenes operativas. Particionada mensualmente por fecha_creacion para soportar alto volumen (~1M filas/mes/particion).';
