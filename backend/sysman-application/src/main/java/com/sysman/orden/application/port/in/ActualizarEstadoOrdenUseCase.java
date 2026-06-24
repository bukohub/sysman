package com.sysman.orden.application.port.in;

import com.sysman.orden.application.command.ActualizarEstadoOrdenCommand;
import com.sysman.orden.domain.model.Orden;

public interface ActualizarEstadoOrdenUseCase {
    Orden ejecutar(ActualizarEstadoOrdenCommand command);
}
