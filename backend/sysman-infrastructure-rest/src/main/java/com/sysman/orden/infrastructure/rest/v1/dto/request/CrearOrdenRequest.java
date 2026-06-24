package com.sysman.orden.infrastructure.rest.v1.dto.request;

import com.sysman.orden.domain.model.TipoOrden;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearOrdenRequest(
        @NotNull Long clienteId,
        @NotNull TipoOrden tipoOrden,
        @NotBlank @Size(max = 1000) String descripcion,
        @NotBlank @Size(max = 300) String direccionServicio,
        @NotBlank @Size(max = 100) String usuario
) {
}
