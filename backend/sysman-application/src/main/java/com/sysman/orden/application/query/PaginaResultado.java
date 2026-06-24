package com.sysman.orden.application.query;

import java.util.List;

public record PaginaResultado<T>(
        List<T> contenido,
        int pagina,
        int tamanioPagina,
        long totalElementos,
        int totalPaginas
) {
}
