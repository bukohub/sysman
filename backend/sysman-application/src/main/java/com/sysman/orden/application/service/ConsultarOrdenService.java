package com.sysman.orden.application.service;

import com.sysman.orden.application.port.in.ConsultarOrdenUseCase;
import com.sysman.orden.application.port.out.OrdenRepositoryPort;
import com.sysman.orden.domain.exception.OrdenNoEncontradaException;
import com.sysman.orden.domain.model.Orden;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultarOrdenService implements ConsultarOrdenUseCase {

    private final OrdenRepositoryPort ordenRepositoryPort;

    public ConsultarOrdenService(OrdenRepositoryPort ordenRepositoryPort) {
        this.ordenRepositoryPort = ordenRepositoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public Orden ejecutar(Long ordenId) {
        return ordenRepositoryPort.buscarPorId(ordenId)
                .orElseThrow(() -> new OrdenNoEncontradaException(ordenId));
    }
}
