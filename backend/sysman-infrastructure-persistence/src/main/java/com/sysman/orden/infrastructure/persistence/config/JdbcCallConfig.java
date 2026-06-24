package com.sysman.orden.infrastructure.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.sql.Types;

@Configuration
public class JdbcCallConfig {

    /**
     * Se declaran los parametros explicitamente (withoutProcedureColumnMetaDataAccess) en
     * vez de dejar que SimpleJdbcCall los descubra via metadata JDBC: es mas portable y
     * evita fallos silenciosos si el driver Oracle no expone correctamente los parametros
     * de un procedimiento empaquetado.
     */
    @Bean
    public SimpleJdbcCall actualizarEstadoOrdenJdbcCall(DataSource dataSource) {
        return new SimpleJdbcCall(dataSource)
                .withCatalogName("ORDEN_PKG")
                .withProcedureName("ACTUALIZAR_ESTADO_ORDEN")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_ORDEN_ID", Types.NUMERIC),
                        new SqlParameter("P_ESTADO_NUEVO", Types.VARCHAR),
                        new SqlParameter("P_VERSION_ESPERADA", Types.NUMERIC),
                        new SqlParameter("P_USUARIO", Types.VARCHAR),
                        new SqlParameter("P_MOTIVO", Types.VARCHAR),
                        new SqlOutParameter("P_ESTADO_RESULTANTE", Types.VARCHAR),
                        new SqlOutParameter("P_VERSION_RESULTANTE", Types.NUMERIC)
                );
    }
}
