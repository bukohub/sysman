package com.sysman.orden.application.command;

import com.sysman.orden.domain.model.TipoOrden;

public record CrearOrdenCommand(
        Long clienteId,
        TipoOrden tipoOrden,
        String descripcion,
        String direccionServicio,
        String usuario
) {
}
