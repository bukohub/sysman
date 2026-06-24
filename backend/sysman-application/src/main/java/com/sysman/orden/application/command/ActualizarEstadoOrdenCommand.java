package com.sysman.orden.application.command;

import com.sysman.orden.domain.model.EstadoOrden;

public record ActualizarEstadoOrdenCommand(
        Long ordenId,
        EstadoOrden estadoNuevo,
        Long versionEsperada,
        String usuario,
        String motivo
) {
}
