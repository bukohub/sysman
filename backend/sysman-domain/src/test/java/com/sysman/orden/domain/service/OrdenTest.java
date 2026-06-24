package com.sysman.orden.domain.service;

import com.sysman.orden.domain.exception.TransicionEstadoInvalidaException;
import com.sysman.orden.domain.model.Cliente;
import com.sysman.orden.domain.model.EstadoOrden;
import com.sysman.orden.domain.model.Orden;
import com.sysman.orden.domain.model.TipoOrden;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrdenTest {

    private Cliente clienteDePrueba() {
        return new Cliente(1L, "1000000001", "Juan Perez", "Calle 10", "3001234567",
                "juan@example.com", LocalDateTime.now());
    }

    @Test
    void crearOrdenQuedaEnEstadoCreadaConHistoricoInicial() {
        Orden orden = Orden.crear(clienteDePrueba(), TipoOrden.INSTALACION, "desc", "direccion", "tester");

        assertThat(orden.getEstado()).isEqualTo(EstadoOrden.CREADA);
        assertThat(orden.getVersion()).isEqualTo(0L);
        assertThat(orden.getHistorico()).hasSize(1);
        assertThat(orden.getHistorico().get(0).getEstadoNuevo()).isEqualTo(EstadoOrden.CREADA);
    }

    @Test
    void cambiarEstadoValidoActualizaEstadoVersionYHistorico() {
        Orden orden = Orden.crear(clienteDePrueba(), TipoOrden.INSTALACION, "desc", "direccion", "tester");

        orden.cambiarEstado(EstadoOrden.ASIGNADA, "tecnico", "asignada a tecnico");

        assertThat(orden.getEstado()).isEqualTo(EstadoOrden.ASIGNADA);
        assertThat(orden.getVersion()).isEqualTo(1L);
        assertThat(orden.getHistorico()).hasSize(2);
        assertThat(orden.getModificadoPor()).isEqualTo("tecnico");
    }

    @Test
    void cambiarEstadoInvalidoLanzaExcepcionYNoModificaLaOrden() {
        Orden orden = Orden.crear(clienteDePrueba(), TipoOrden.INSTALACION, "desc", "direccion", "tester");

        assertThrows(TransicionEstadoInvalidaException.class,
                () -> orden.cambiarEstado(EstadoOrden.COMPLETADA, "tecnico", "salto invalido"));

        assertThat(orden.getEstado()).isEqualTo(EstadoOrden.CREADA);
        assertThat(orden.getVersion()).isEqualTo(0L);
        assertThat(orden.getHistorico()).hasSize(1);
    }
}
