package com.sysman.orden.application.port.out;

import com.sysman.orden.application.query.FiltroOrdenQuery;
import com.sysman.orden.application.query.PaginaResultado;
import com.sysman.orden.domain.model.Orden;

import java.util.Optional;

public interface OrdenRepositoryPort {

    Orden guardar(Orden orden);

    Optional<Orden> buscarPorId(Long id);

    PaginaResultado<Orden> buscarConFiltros(FiltroOrdenQuery filtro, int pagina, int tamanioPagina);
}
