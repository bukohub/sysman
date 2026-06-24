package com.sysman.orden.infrastructure.persistence.adapter;

import com.sysman.orden.application.port.out.ResultadoActualizacionEstado;
import com.sysman.orden.domain.exception.ConflictoConcurrenciaException;
import com.sysman.orden.domain.exception.TransicionEstadoInvalidaException;
import com.sysman.orden.domain.model.EstadoOrden;
import com.sysman.orden.infrastructure.persistence.TestPersistenceApplication;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
@Testcontainers
@SpringBootTest(classes = TestPersistenceApplication.class)
class ActualizarEstadoOrdenJdbcAdapterIT {

    @Container
    static final OracleContainer ORACLE = new OracleContainer("gvenzl/oracle-xe:21-slim");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", ORACLE::getJdbcUrl);
        registry.add("spring.datasource.username", ORACLE::getUsername);
        registry.add("spring.datasource.password", ORACLE::getPassword);
    }

    @Autowired
    private ActualizarEstadoOrdenJdbcAdapter adapter;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    private Long crearClienteYOrdenDePrueba() {
        jdbcTemplate.update(
                "INSERT INTO cliente (numero_identificacion, nombre, creado_por) VALUES (?, ?, ?)",
                "IT-" + System.nanoTime(), "Cliente IT", "test");
        Long clienteId = jdbcTemplate.queryForObject(
                "SELECT id FROM cliente ORDER BY id DESC FETCH FIRST 1 ROWS ONLY", Long.class);

        jdbcTemplate.update(
                "INSERT INTO orden (cliente_id, tipo_orden, estado, creado_por) VALUES (?, 'INSTALACION', 'CREADA', ?)",
                clienteId, "test");
        return jdbcTemplate.queryForObject(
                "SELECT id FROM orden ORDER BY id DESC FETCH FIRST 1 ROWS ONLY", Long.class);
    }

    @Test
    void actualizaEstadoYRegistraHistoricoParaTransicionValida() {
        Long ordenId = crearClienteYOrdenDePrueba();

        ResultadoActualizacionEstado resultado = adapter.actualizar(
                ordenId, EstadoOrden.ASIGNADA, 0L, "tester", "asignacion de prueba");

        assertThat(resultado.estadoResultante()).isEqualTo(EstadoOrden.ASIGNADA);
        assertThat(resultado.versionResultante()).isEqualTo(1L);

        Integer historicos = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM orden_historico WHERE orden_id = ?", Integer.class, ordenId);
        assertThat(historicos).isEqualTo(1);
    }

    @Test
    void rechazaTransicionInvalidaConExcepcionDeDominio() {
        Long ordenId = crearClienteYOrdenDePrueba();

        assertThrows(TransicionEstadoInvalidaException.class,
                () -> adapter.actualizar(ordenId, EstadoOrden.COMPLETADA, 0L, "tester", "salto invalido"));
    }

    @Test
    void rechazaVersionObsoletaConConflictoDeConcurrencia() {
        Long ordenId = crearClienteYOrdenDePrueba();

        assertThrows(ConflictoConcurrenciaException.class,
                () -> adapter.actualizar(ordenId, EstadoOrden.ASIGNADA, 99L, "tester", "version incorrecta"));
    }
}
