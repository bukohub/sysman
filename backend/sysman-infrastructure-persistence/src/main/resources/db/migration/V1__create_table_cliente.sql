CREATE TABLE cliente (
    id                      NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero_identificacion   VARCHAR2(20)  NOT NULL,
    nombre                  VARCHAR2(200) NOT NULL,
    direccion               VARCHAR2(300),
    telefono                VARCHAR2(20),
    email                   VARCHAR2(150),
    creado_por              VARCHAR2(100) NOT NULL,
    fecha_creacion          TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    modificado_por          VARCHAR2(100),
    fecha_modificacion      TIMESTAMP,
    version                 NUMBER        DEFAULT 0 NOT NULL,
    CONSTRAINT uq_cliente_identificacion UNIQUE (numero_identificacion)
);

COMMENT ON TABLE cliente IS 'Clientes de la empresa de servicios publicos';
