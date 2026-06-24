package com.sysman.orden.application.service;

import com.sysman.orden.application.port.out.OrdenRepositoryPort;
import com.sysman.orden.application.query.FiltroOrdenQuery;
import com.sysman.orden.application.query.PaginaResultado;
import com.sysman.orden.domain.model.EstadoOrden;
import com.sysman.orden.domain.model.Orden;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarOrdenesServiceTest {

    @Mock
    private OrdenRepositoryPort ordenRepositoryPort;

    @Test
    void delegaElFiltroYLaPaginacionAlRepositorio() {
        FiltroOrdenQuery filtro = new FiltroOrdenQuery(EstadoOrden.CREADA, null, null);
        PaginaResultado<Orden> paginaEsperada = new PaginaResultado<>(List.of(), 0, 20, 0, 0);
        when(ordenRepositoryPort.buscarConFiltros(filtro, 0, 20)).thenReturn(paginaEsperada);

        ListarOrdenesService service = new ListarOrdenesService(ordenRepositoryPort);

        PaginaResultado<Orden> resultado = service.ejecutar(filtro, 0, 20);

        assertThat(resultado).isSameAs(paginaEsperada);
    }
}
