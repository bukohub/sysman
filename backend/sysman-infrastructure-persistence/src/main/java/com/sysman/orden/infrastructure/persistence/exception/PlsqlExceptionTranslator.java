package com.sysman.orden.infrastructure.persistence.exception;

import com.sysman.orden.domain.exception.ConflictoConcurrenciaException;
import com.sysman.orden.domain.exception.OrdenNoEncontradaException;
import com.sysman.orden.domain.exception.TransicionEstadoInvalidaException;
import com.sysman.orden.domain.model.EstadoOrden;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Traduce los codigos de error Oracle ORA-20001/20002/20003 (lanzados por
 * orden_pkg.actualizar_estado_orden via RAISE_APPLICATION_ERROR) a excepciones de dominio.
 */
public final class PlsqlExceptionTranslator {

    private static final int ORA_TRANSICION_INVALIDA = 20001;
    private static final int ORA_ORDEN_NO_ENCONTRADA = 20002;
    private static final int ORA_VERSION_OBSOLETA = 20003;
    private static final Pattern VERSION_ACTUAL_PATTERN = Pattern.compile("actual=(\\d+)");

    private PlsqlExceptionTranslator() {
    }

    public static RuntimeException traducir(DataAccessException ex, Long ordenId,
                                              EstadoOrden estadoSolicitado, Long versionEsperada) {
        SQLException sqlException = encontrarSqlException(ex);
        if (sqlException == null) {
            return ex;
        }

        return switch (sqlException.getErrorCode()) {
            case ORA_ORDEN_NO_ENCONTRADA -> new OrdenNoEncontradaException(ordenId);
            case ORA_TRANSICION_INVALIDA -> new TransicionEstadoInvalidaException(null, estadoSolicitado);
            case ORA_VERSION_OBSOLETA ->
                new ConflictoConcurrenciaException(ordenId, versionEsperada, extraerVersionActual(sqlException));
            default -> ex;
        };
    }

    private static Long extraerVersionActual(SQLException sqlException) {
        Matcher matcher = VERSION_ACTUAL_PATTERN.matcher(sqlException.getMessage());
        return matcher.find() ? Long.valueOf(matcher.group(1)) : null;
    }

    private static SQLException encontrarSqlException(Throwable throwable) {
        Throwable actual = throwable;
        while (actual != null) {
            if (actual instanceof SQLException sqlException) {
                return sqlException;
            }
            actual = actual.getCause();
        }
        return null;
    }
}
