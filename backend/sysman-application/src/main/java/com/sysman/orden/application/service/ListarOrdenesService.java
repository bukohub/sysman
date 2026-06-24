package com.sysman.orden.application.service;

import com.sysman.orden.application.port.in.ListarOrdenesUseCase;
import com.sysman.orden.application.port.out.OrdenRepositoryPort;
import com.sysman.orden.application.query.FiltroOrdenQuery;
import com.sysman.orden.application.query.PaginaResultado;
import com.sysman.orden.domain.model.Orden;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListarOrdenesService implements ListarOrdenesUseCase {

    private final OrdenRepositoryPort ordenRepositoryPort;

    public ListarOrdenesService(OrdenRepositoryPort ordenRepositoryPort) {
        this.ordenRepositoryPort = ordenRepositoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResultado<Orden> ejecutar(FiltroOrdenQuery filtro, int pagina, int tamanioPagina) {
        return ordenRepositoryPort.buscarConFiltros(filtro, pagina, tamanioPagina);
    }
}
