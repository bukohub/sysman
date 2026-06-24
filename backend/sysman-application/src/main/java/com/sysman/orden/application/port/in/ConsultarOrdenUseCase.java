package com.sysman.orden.application.port.in;

import com.sysman.orden.domain.model.Orden;

public interface ConsultarOrdenUseCase {
    Orden ejecutar(Long ordenId);
}
