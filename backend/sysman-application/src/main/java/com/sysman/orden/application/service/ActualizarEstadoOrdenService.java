package com.sysman.orden.application.service;

import com.sysman.orden.application.command.ActualizarEstadoOrdenCommand;
import com.sysman.orden.application.port.in.ActualizarEstadoOrdenUseCase;
import com.sysman.orden.application.port.out.ActualizarEstadoOrdenPort;
import com.sysman.orden.application.port.out.OrdenRepositoryPort;
import com.sysman.orden.domain.exception.OrdenNoEncontradaException;
import com.sysman.orden.domain.model.Orden;
import com.sysman.orden.domain.service.ValidadorTransicionEstado;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActualizarEstadoOrdenService implements ActualizarEstadoOrdenUseCase {

    private final OrdenRepositoryPort ordenRepositoryPort;
    private final ActualizarEstadoOrdenPort actualizarEstadoOrdenPort;

    public ActualizarEstadoOrdenService(OrdenRepositoryPort ordenRepositoryPort,
                                         ActualizarEstadoOrdenPort actualizarEstadoOrdenPort) {
        this.ordenRepositoryPort = ordenRepositoryPort;
        this.actualizarEstadoOrdenPort = actualizarEstadoOrdenPort;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Orden ejecutar(ActualizarEstadoOrdenCommand command) {
        Orden ordenActual = ordenRepositoryPort.buscarPorId(command.ordenId())
                .orElseThrow(() -> new OrdenNoEncontradaException(command.ordenId()));

        // Validacion temprana en Java (defensa en profundidad): evita una llamada
        // innecesaria al procedimiento PL/SQL si la transicion es obviamente invalida.
        // El procedimiento vuelve a validar dentro de la transaccion de BD por si hay
        // otro canal de escritura directo.
        ValidadorTransicionEstado.validar(ordenActual.getEstado(), command.estadoNuevo());

        actualizarEstadoOrdenPort.actualizar(command.ordenId(), command.estadoNuevo(),
                command.versionEsperada(), command.usuario(), command.motivo());

        // El procedimiento PL/SQL modifico la fila por fuera del contexto de persistencia
        // de Hibernate; se relee para devolver el estado real y evitar version cacheada.
        return ordenRepositoryPort.buscarPorId(command.ordenId())
                .orElseThrow(() -> new OrdenNoEncontradaException(command.ordenId()));
    }
}
