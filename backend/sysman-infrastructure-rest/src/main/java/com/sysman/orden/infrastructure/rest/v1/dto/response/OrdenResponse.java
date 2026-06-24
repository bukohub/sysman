package com.sysman.orden.infrastructure.rest.v1.dto.response;

import com.sysman.orden.domain.model.EstadoOrden;
import com.sysman.orden.domain.model.TipoOrden;

import java.time.LocalDateTime;
import java.util.List;

public record OrdenResponse(
        Long id,
        ClienteResumenResponse cliente,
        TipoOrden tipoOrden,
        EstadoOrden estado,
        String descripcion,
        String direccionServicio,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion,
        Long version,
        List<OrdenHistoricoResponse> historico
) {
}
