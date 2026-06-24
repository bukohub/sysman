package com.sysman.orden.application.port.out;

import com.sysman.orden.domain.model.EstadoOrden;

public record ResultadoActualizacionEstado(
        EstadoOrden estadoResultante,
        Long versionResultante
) {
}
