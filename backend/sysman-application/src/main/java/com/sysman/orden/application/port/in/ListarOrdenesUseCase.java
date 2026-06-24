package com.sysman.orden.application.port.in;

import com.sysman.orden.application.query.FiltroOrdenQuery;
import com.sysman.orden.application.query.PaginaResultado;
import com.sysman.orden.domain.model.Orden;

public interface ListarOrdenesUseCase {
    PaginaResultado<Orden> ejecutar(FiltroOrdenQuery filtro, int pagina, int tamanioPagina);
}
