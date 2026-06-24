package com.sysman.orden.application.service;

import com.sysman.orden.application.command.CrearOrdenCommand;
import com.sysman.orden.application.port.in.CrearOrdenUseCase;
import com.sysman.orden.application.port.out.ClienteRepositoryPort;
import com.sysman.orden.application.port.out.OrdenRepositoryPort;
import com.sysman.orden.domain.exception.ClienteNoEncontradoException;
import com.sysman.orden.domain.model.Cliente;
import com.sysman.orden.domain.model.Orden;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrearOrdenService implements CrearOrdenUseCase {

    private final OrdenRepositoryPort ordenRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;

    public CrearOrdenService(OrdenRepositoryPort ordenRepositoryPort, ClienteRepositoryPort clienteRepositoryPort) {
        this.ordenRepositoryPort = ordenRepositoryPort;
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Orden ejecutar(CrearOrdenCommand command) {
        Cliente cliente = clienteRepositoryPort.buscarPorId(command.clienteId())
                .orElseThrow(() -> new ClienteNoEncontradoException(command.clienteId()));

        Orden orden = Orden.crear(cliente, command.tipoOrden(), command.descripcion(),
                command.direccionServicio(), command.usuario());

        return ordenRepositoryPort.guardar(orden);
    }
}
