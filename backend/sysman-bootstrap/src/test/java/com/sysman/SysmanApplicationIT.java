package com.sysman;

import com.sysman.orden.infrastructure.rest.v1.dto.request.ActualizarEstadoOrdenRequest;
import com.sysman.orden.infrastructure.rest.v1.dto.request.CrearOrdenRequest;
import com.sysman.orden.infrastructure.rest.v1.dto.response.OrdenResponse;
import com.sysman.orden.domain.model.EstadoOrden;
import com.sysman.orden.domain.model.TipoOrden;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@Testcontainers
@SpringBootTest(classes = SysmanApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SysmanApplicationIT {

    @Container
    static final OracleContainer ORACLE = new OracleContainer("gvenzl/oracle-xe:21-slim");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", ORACLE::getJdbcUrl);
        registry.add("spring.datasource.username", ORACLE::getUsername);
        registry.add("spring.datasource.password", ORACLE::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long crearClienteDePrueba() {
        jdbcTemplate.update(
                "INSERT INTO cliente (numero_identificacion, nombre, creado_por) VALUES (?, ?, ?)",
                "E2E-" + System.nanoTime(), "Cliente E2E", "test");
        return jdbcTemplate.queryForObject(
                "SELECT id FROM cliente ORDER BY id DESC FETCH FIRST 1 ROWS ONLY", Long.class);
    }

    @Test
    void flujoCompletoCrearConsultarYActualizarEstado() {
        Long clienteId = crearClienteDePrueba();

        CrearOrdenRequest crearRequest = new CrearOrdenRequest(
                clienteId, TipoOrden.INSTALACION, "Instalacion de prueba e2e", "Calle 1", "tester");
        ResponseEntity<OrdenResponse> creada = restTemplate.postForEntity("/api/v1/orden", crearRequest, OrdenResponse.class);
        assertThat(creada.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(creada.getBody()).isNotNull();
        Long ordenId = creada.getBody().id();
        assertThat(creada.getBody().estado()).isEqualTo(EstadoOrden.CREADA);

        ResponseEntity<OrdenResponse> consultada = restTemplate.getForEntity("/api/v1/orden/" + ordenId, OrdenResponse.class);
        assertThat(consultada.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(consultada.getBody().historico()).hasSize(1);

        ActualizarEstadoOrdenRequest actualizarRequest = new ActualizarEstadoOrdenRequest(
                EstadoOrden.ASIGNADA, 0L, "tecnico", "asignacion e2e");
        ResponseEntity<OrdenResponse> actualizada = restTemplate.exchange(
                "/api/v1/orden/" + ordenId + "/estado",
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(actualizarRequest),
                OrdenResponse.class);
        assertThat(actualizada.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualizada.getBody().estado()).isEqualTo(EstadoOrden.ASIGNADA);
        assertThat(actualizada.getBody().historico()).hasSize(2);

        ActualizarEstadoOrdenRequest transicionInvalida = new ActualizarEstadoOrdenRequest(
                EstadoOrden.COMPLETADA, 1L, "tecnico", "salto invalido");
        ResponseEntity<String> rechazada = restTemplate.exchange(
                "/api/v1/orden/" + ordenId + "/estado",
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(transicionInvalida),
                String.class);
        assertThat(rechazada.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        ActualizarEstadoOrdenRequest versionObsoleta = new ActualizarEstadoOrdenRequest(
                EstadoOrden.EN_PROCESO, 0L, "tecnico", "version vieja");
        ResponseEntity<String> conflicto = restTemplate.exchange(
                "/api/v1/orden/" + ordenId + "/estado",
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(versionObsoleta),
                String.class);
        assertThat(conflicto.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
