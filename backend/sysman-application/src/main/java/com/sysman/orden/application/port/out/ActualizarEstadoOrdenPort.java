package com.sysman.orden.application.port.out;

import com.sysman.orden.domain.model.EstadoOrden;

/**
 * Puerto especifico hacia el procedimiento PL/SQL orden_pkg.actualizar_estado_orden.
 * Bypasea JPA deliberadamente: la transicion de estado es la unica via de escritura
 * del campo "estado" de una orden, y debe quedar serializada estrictamente en BD.
 */
public interface ActualizarEstadoOrdenPort {

    ResultadoActualizacionEstado actualizar(Long ordenId, EstadoOrden nuevoEstado,
                                            Long versionEsperada, String usuario, String motivo);
}
