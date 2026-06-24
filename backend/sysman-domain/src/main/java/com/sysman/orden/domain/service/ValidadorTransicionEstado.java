package com.sysman.orden.domain.service;

import com.sysman.orden.domain.exception.TransicionEstadoInvalidaException;
import com.sysman.orden.domain.model.EstadoOrden;

import java.util.Map;
import java.util.Set;

/**
 * Espejo en Java de la tabla ORDEN_TRANSICION_VALIDA usada por el procedimiento PL/SQL
 * orden_pkg.actualizar_estado_orden. Mantener ambas listas sincronizadas si cambian las reglas.
 */
public final class ValidadorTransicionEstado {

    private static final Map<EstadoOrden, Set<EstadoOrden>> TRANSICIONES_VALIDAS = Map.of(
            EstadoOrden.CREADA, Set.of(EstadoOrden.ASIGNADA, EstadoOrden.CANCELADA, EstadoOrden.RECHAZADA),
            EstadoOrden.ASIGNADA, Set.of(EstadoOrden.EN_PROCESO, EstadoOrden.CANCELADA),
            EstadoOrden.EN_PROCESO, Set.of(EstadoOrden.COMPLETADA, EstadoOrden.CANCELADA),
            EstadoOrden.COMPLETADA, Set.of(),
            EstadoOrden.CANCELADA, Set.of(),
            EstadoOrden.RECHAZADA, Set.of(EstadoOrden.CREADA)
    );

    private ValidadorTransicionEstado() {
    }

    public static void validar(EstadoOrden actual, EstadoOrden nuevo) {
        if (!TRANSICIONES_VALIDAS.getOrDefault(actual, Set.of()).contains(nuevo)) {
            throw new TransicionEstadoInvalidaException(actual, nuevo);
        }
    }
}
