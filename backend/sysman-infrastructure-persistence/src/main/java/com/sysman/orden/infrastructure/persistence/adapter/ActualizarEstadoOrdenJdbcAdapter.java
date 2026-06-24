package com.sysman.orden.infrastructure.persistence.adapter;

import com.sysman.orden.application.port.out.ActualizarEstadoOrdenPort;
import com.sysman.orden.application.port.out.ResultadoActualizacionEstado;
import com.sysman.orden.domain.model.EstadoOrden;
import com.sysman.orden.infrastructure.persistence.exception.PlsqlExceptionTranslator;
import jakarta.persistence.EntityManager;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ActualizarEstadoOrdenJdbcAdapter implements ActualizarEstadoOrdenPort {

    private final SimpleJdbcCall actualizarEstadoOrdenJdbcCall;
    private final EntityManager entityManager;

    public ActualizarEstadoOrdenJdbcAdapter(SimpleJdbcCall actualizarEstadoOrdenJdbcCall, EntityManager entityManager) {
        this.actualizarEstadoOrdenJdbcCall = actualizarEstadoOrdenJdbcCall;
        this.entityManager = entityManager;
    }

    @Override
    public ResultadoActualizacionEstado actualizar(Long ordenId, EstadoOrden nuevoEstado,
                                                    Long versionEsperada, String usuario, String motivo) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("P_ORDEN_ID", ordenId)
                .addValue("P_ESTADO_NUEVO", nuevoEstado.name())
                .addValue("P_VERSION_ESPERADA", versionEsperada)
                .addValue("P_USUARIO", usuario)
                .addValue("P_MOTIVO", motivo);

        try {
            Map<String, Object> out = actualizarEstadoOrdenJdbcCall.execute(params);
            EstadoOrden estadoResultante = EstadoOrden.valueOf((String) out.get("P_ESTADO_RESULTANTE"));
            Long versionResultante = ((Number) out.get("P_VERSION_RESULTANTE")).longValue();

            // El procedimiento PL/SQL escribio via JDBC plano, por fuera del contexto de
            // persistencia de Hibernate. Se limpia ese contexto (dentro de la misma
            // transaccion) para que cualquier lectura posterior de esta orden en la misma
            // transaccion golpee la base de datos en vez de devolver la entidad cacheada
            // con el estado/version anteriores.
            entityManager.clear();

            return new ResultadoActualizacionEstado(estadoResultante, versionResultante);
        } catch (DataAccessException ex) {
            throw PlsqlExceptionTranslator.traducir(ex, ordenId, nuevoEstado, versionEsperada);
        }
    }
}
