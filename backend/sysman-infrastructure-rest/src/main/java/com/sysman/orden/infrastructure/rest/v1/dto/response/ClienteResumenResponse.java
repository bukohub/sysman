package com.sysman.orden.infrastructure.rest.v1.dto.response;

public record ClienteResumenResponse(
        Long id,
        String numeroIdentificacion,
        String nombre
) {
}
