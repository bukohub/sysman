package com.sysman.orden.infrastructure.rest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Configuracion minima usada SOLO por los tests @WebMvcTest de este modulo, que no
 * tiene una @SpringBootApplication propia (esa vive en sysman-bootstrap).
 */
@SpringBootApplication
public class TestRestApplication {
}
