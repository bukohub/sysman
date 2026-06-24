-- Indices GLOBALES (no LOCAL): los filtros del listado (estado + rango de fechas) cruzan
-- particiones, por lo que un indice LOCAL obligaria a Oracle a escanear el indice de cada
-- particion candidata. El costo de esta decision es un mantenimiento mas caro en
-- DROP PARTITION/purga (requiere UPDATE INDEXES o rebuild), documentado en docs/ARQUITECTURA.md.

CREATE INDEX ix_orden_estado_fecha ON orden (estado, fecha_creacion);
CREATE INDEX ix_orden_fecha_creacion ON orden (fecha_creacion DESC);
CREATE INDEX ix_orden_cliente ON orden (cliente_id);

CREATE INDEX ix_historico_orden_fecha ON orden_historico (orden_id, fecha_cambio DESC);
