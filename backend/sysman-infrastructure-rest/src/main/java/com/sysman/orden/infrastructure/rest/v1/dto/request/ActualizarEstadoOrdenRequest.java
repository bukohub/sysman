package com.sysman.orden.infrastructure.rest.v1.dto.request;

import com.sysman.orden.domain.model.EstadoOrden;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActualizarEstadoOrdenRequest(
        @NotNull EstadoOrden estadoNuevo,
        @NotNull Long version,
        @NotBlank @Size(max = 100) String usuario,
        @Size(max = 500) String motivo
) {
}
