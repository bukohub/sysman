package com.sysman.orden.application.service;

import com.sysman.orden.application.port.in.BuscarClientesUseCase;
import com.sysman.orden.application.port.out.ClienteRepositoryPort;
import com.sysman.orden.domain.model.Cliente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BuscarClientesService implements BuscarClientesUseCase {

    private static final int LIMITE_MAXIMO = 20;

    private final ClienteRepositoryPort clienteRepositoryPort;

    public BuscarClientesService(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> ejecutar(String texto, int limite) {
        String textoBusqueda = texto == null ? "" : texto.trim();
        int limiteEfectivo = Math.min(Math.max(limite, 1), LIMITE_MAXIMO);
        return clienteRepositoryPort.buscarPorTexto(textoBusqueda, limiteEfectivo);
    }
}
