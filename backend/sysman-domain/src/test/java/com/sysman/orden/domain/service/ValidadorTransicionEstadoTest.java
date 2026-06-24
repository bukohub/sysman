package com.sysman.orden.domain.service;

import com.sysman.orden.domain.exception.TransicionEstadoInvalidaException;
import com.sysman.orden.domain.model.EstadoOrden;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorTransicionEstadoTest {

    @ParameterizedTest
    @CsvSource({
            "CREADA, ASIGNADA",
            "CREADA, CANCELADA",
            "CREADA, RECHAZADA",
            "ASIGNADA, EN_PROCESO",
            "ASIGNADA, CANCELADA",
            "EN_PROCESO, COMPLETADA",
            "EN_PROCESO, CANCELADA",
            "RECHAZADA, CREADA"
    })
    void permiteTransicionesValidas(EstadoOrden origen, EstadoOrden destino) {
        assertDoesNotThrow(() -> ValidadorTransicionEstado.validar(origen, destino));
    }

    @ParameterizedTest
    @CsvSource({
            "CREADA, EN_PROCESO",
            "CREADA, COMPLETADA",
            "ASIGNADA, RECHAZADA",
            "ASIGNADA, COMPLETADA",
            "EN_PROCESO, ASIGNADA",
            "EN_PROCESO, RECHAZADA",
            "COMPLETADA, CREADA",
            "COMPLETADA, CANCELADA",
            "CANCELADA, CREADA",
            "RECHAZADA, ASIGNADA",
            "RECHAZADA, EN_PROCESO"
    })
    void rechazaTransicionesInvalidas(EstadoOrden origen, EstadoOrden destino) {
        TransicionEstadoInvalidaException ex = assertThrows(TransicionEstadoInvalidaException.class,
                () -> ValidadorTransicionEstado.validar(origen, destino));
        assertEquals(origen, ex.getEstadoActual());
        assertEquals(destino, ex.getEstadoSolicitado());
    }
}
