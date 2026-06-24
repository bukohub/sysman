package com.sysman.orden.application.port.in;

import com.sysman.orden.application.command.CrearOrdenCommand;
import com.sysman.orden.domain.model.Orden;

public interface CrearOrdenUseCase {
    Orden ejecutar(CrearOrdenCommand command);
}
