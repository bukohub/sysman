package com.sysman.orden.application.service;

import com.sysman.orden.application.command.ActualizarEstadoOrdenCommand;
import com.sysman.orden.application.port.out.ActualizarEstadoOrdenPort;
import com.sysman.orden.application.port.out.OrdenRepositoryPort;
import com.sysman.orden.application.port.out.ResultadoActualizacionEstado;
import com.sysman.orden.domain.exception.OrdenNoEncontradaException;
import com.sysman.orden.domain.exception.TransicionEstadoInvalidaException;
import com.sysman.orden.domain.model.Cliente;
import com.sysman.orden.domain.model.EstadoOrden;
import com.sysman.orden.domain.model.Orden;
import com.sysman.orden.domain.model.TipoOrden;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActualizarEstadoOrdenServiceTest {

    @Mock
    private OrdenRepositoryPort ordenRepositoryPort;

    @Mock
    private ActualizarEstadoOrdenPort actualizarEstadoOrdenPort;

    private Orden ordenCreada() {
        Cliente cliente = new Cliente(1L, "1000000001", "Juan Perez", "dir", "tel", "mail", LocalDateTime.now());
        return Orden.crear(cliente, TipoOrden.INSTALACION, "desc", "dir", "tester");
    }

    @Test
    void actualizaEstadoInvocandoElProcedimientoYReleeLaOrden() {
        Orden ordenAntes = ordenCreada();
        Orden ordenDespues = ordenCreada();
        ordenDespues.cambiarEstado(EstadoOrden.ASIGNADA, "tecnico", "asignada");

        when(ordenRepositoryPort.buscarPorId(1L))
                .thenReturn(Optional.of(ordenAntes))
                .thenReturn(Optional.of(ordenDespues));
        when(actualizarEstadoOrdenPort.actualizar(1L, EstadoOrden.ASIGNADA, 0L, "tecnico", "asignada"))
                .thenReturn(new ResultadoActualizacionEstado(EstadoOrden.ASIGNADA, 1L));

        ActualizarEstadoOrdenService service = new ActualizarEstadoOrdenService(ordenRepositoryPort, actualizarEstadoOrdenPort);
        ActualizarEstadoOrdenCommand command = new ActualizarEstadoOrdenCommand(1L, EstadoOrden.ASIGNADA, 0L, "tecnico", "asignada");

        Orden resultado = service.ejecutar(command);

        assertThat(resultado.getEstado()).isEqualTo(EstadoOrden.ASIGNADA);
        verify(actualizarEstadoOrdenPort).actualizar(1L, EstadoOrden.ASIGNADA, 0L, "tecnico", "asignada");
    }

    @Test
    void rechazaTransicionInvalidaSinLlamarAlProcedimiento() {
        when(ordenRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(ordenCreada()));

        ActualizarEstadoOrdenService service = new ActualizarEstadoOrdenService(ordenRepositoryPort, actualizarEstadoOrdenPort);
        ActualizarEstadoOrdenCommand command = new ActualizarEstadoOrdenCommand(1L, EstadoOrden.COMPLETADA, 0L, "tecnico", "salto invalido");

        assertThrows(TransicionEstadoInvalidaException.class, () -> service.ejecutar(command));
        verify(actualizarEstadoOrdenPort, never())
                .actualizar(anyLong(), any(EstadoOrden.class), anyLong(), anyString(), anyString());
    }

    @Test
    void lanzaExcepcionCuandoLaOrdenNoExiste() {
        when(ordenRepositoryPort.buscarPorId(404L)).thenReturn(Optional.empty());

        ActualizarEstadoOrdenService service = new ActualizarEstadoOrdenService(ordenRepositoryPort, actualizarEstadoOrdenPort);
        ActualizarEstadoOrdenCommand command = new ActualizarEstadoOrdenCommand(404L, EstadoOrden.ASIGNADA, 0L, "tecnico", "motivo");

        assertThrows(OrdenNoEncontradaException.class, () -> service.ejecutar(command));
    }
}
