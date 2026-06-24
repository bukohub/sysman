package com.sysman.orden.infrastructure.rest.v1.dto.response;

import com.sysman.orden.domain.model.EstadoOrden;

import java.time.LocalDateTime;

public record OrdenHistoricoResponse(
        Long id,
        EstadoOrden estadoAnterior,
        EstadoOrden estadoNuevo,
        LocalDateTime fechaCambio,
        String usuario,
        String motivo
) {
}
