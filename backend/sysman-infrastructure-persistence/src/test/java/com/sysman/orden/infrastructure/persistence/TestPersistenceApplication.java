package com.sysman.orden.infrastructure.persistence;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicacion minima usada SOLO por los tests de integracion de este modulo
 * (Testcontainers + Flyway + JPA + SimpleJdbcCall), nunca empaquetada en produccion.
 */
@SpringBootApplication
public class TestPersistenceApplication {
}
