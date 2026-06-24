package com.sysman.orden.infrastructure.rest.v1.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> contenido,
        int pagina,
        int tamanioPagina,
        long totalElementos,
        int totalPaginas
) {
}
