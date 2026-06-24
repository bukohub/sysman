package com.sysman.orden.application.service;

import com.sysman.orden.application.command.CrearOrdenCommand;
import com.sysman.orden.application.port.out.ClienteRepositoryPort;
import com.sysman.orden.application.port.out.OrdenRepositoryPort;
import com.sysman.orden.domain.exception.ClienteNoEncontradoException;
import com.sysman.orden.domain.model.Cliente;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearOrdenServiceTest {

    @Mock
    private OrdenRepositoryPort ordenRepositoryPort;

    @Mock
    private ClienteRepositoryPort clienteRepositoryPort;

    @Test
    void creaOrdenCuandoClienteExiste() {
        Cliente cliente = new Cliente(1L, "1000000001", "Juan Perez", "dir", "tel", "mail", LocalDateTime.now());
        when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(ordenRepositoryPort.guardar(any(Orden.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CrearOrdenService service = new CrearOrdenService(ordenRepositoryPort, clienteRepositoryPort);
        CrearOrdenCommand command = new CrearOrdenCommand(1L, TipoOrden.INSTALACION, "desc", "dir", "tester");

        Orden resultado = service.ejecutar(command);

        assertThat(resultado.getCliente().getId()).isEqualTo(1L);
        verify(ordenRepositoryPort).guardar(any(Orden.class));
    }

    @Test
    void lanzaExcepcionCuandoClienteNoExiste() {
        when(clienteRepositoryPort.buscarPorId(99L)).thenReturn(Optional.empty());

        CrearOrdenService service = new CrearOrdenService(ordenRepositoryPort, clienteRepositoryPort);
        CrearOrdenCommand command = new CrearOrdenCommand(99L, TipoOrden.INSTALACION, "desc", "dir", "tester");

        assertThrows(ClienteNoEncontradoException.class, () -> service.ejecutar(command));
        verifyNoInteractions(ordenRepositoryPort);
    }
}
