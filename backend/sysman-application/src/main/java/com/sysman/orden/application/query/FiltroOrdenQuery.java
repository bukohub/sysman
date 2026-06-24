package com.sysman.orden.application.query;

import com.sysman.orden.domain.model.EstadoOrden;

import java.time.LocalDateTime;

public record FiltroOrdenQuery(
        EstadoOrden estado,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin
) {
}
