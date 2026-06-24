package com.sysman.orden.infrastructure.rest.v1.dto.response;

import com.sysman.orden.domain.model.EstadoOrden;
import com.sysman.orden.domain.model.TipoOrden;

import java.time.LocalDateTime;

public record OrdenResumenResponse(
        Long id,
        Long clienteId,
        String nombreCliente,
        TipoOrden tipoOrden,
        EstadoOrden estado,
        LocalDateTime fechaCreacion
) {
}
