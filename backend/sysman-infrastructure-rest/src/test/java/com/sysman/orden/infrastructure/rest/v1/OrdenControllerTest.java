package com.sysman.orden.infrastructure.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sysman.orden.application.port.in.ActualizarEstadoOrdenUseCase;
import com.sysman.orden.application.port.in.ConsultarOrdenUseCase;
import com.sysman.orden.application.port.in.CrearOrdenUseCase;
import com.sysman.orden.application.port.in.ListarOrdenesUseCase;
import com.sysman.orden.application.query.PaginaResultado;
import com.sysman.orden.domain.exception.ConflictoConcurrenciaException;
import com.sysman.orden.domain.exception.OrdenNoEncontradaException;
import com.sysman.orden.domain.exception.TransicionEstadoInvalidaException;
import com.sysman.orden.domain.model.Cliente;
import com.sysman.orden.domain.model.EstadoOrden;
import com.sysman.orden.domain.model.Orden;
import com.sysman.orden.domain.model.TipoOrden;
import com.sysman.orden.infrastructure.rest.config.CorrelationIdFilter;
import com.sysman.orden.infrastructure.rest.exception.GlobalExceptionHandler;
import com.sysman.orden.infrastructure.rest.v1.mapper.OrdenRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrdenController.class)
@Import({GlobalExceptionHandler.class, OrdenRestMapper.class, CorrelationIdFilter.class})
class OrdenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CrearOrdenUseCase crearOrdenUseCase;

    @MockBean
    private ConsultarOrdenUseCase consultarOrdenUseCase;

    @MockBean
    private ActualizarEstadoOrdenUseCase actualizarEstadoOrdenUseCase;

    @MockBean
    private ListarOrdenesUseCase listarOrdenesUseCase;

    private Orden ordenDePrueba() {
        Cliente cliente = new Cliente(1L, "1000000001", "Juan Perez", "dir", "tel", "mail", LocalDateTime.now());
        return Orden.crear(cliente, TipoOrden.INSTALACION, "desc", "direccion", "tester");
    }

    @Test
    void crearOrdenDevuelve201ConUbicacion() throws Exception {
        Orden orden = ordenDePrueba();
        when(crearOrdenUseCase.ejecutar(any())).thenReturn(orden);

        String body = """
                {"clienteId":1,"tipoOrden":"INSTALACION","descripcion":"desc","direccionServicio":"dir","usuario":"tester"}
                """;

        mockMvc.perform(post("/api/v1/orden").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("CREADA"));
    }

    @Test
    void crearOrdenConCamposInvalidosDevuelve400() throws Exception {
        String body = """
                {"clienteId":null,"tipoOrden":"INSTALACION","descripcion":"","direccionServicio":"dir","usuario":"tester"}
                """;

        mockMvc.perform(post("/api/v1/orden").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void consultarOrdenInexistenteDevuelve404() throws Exception {
        when(consultarOrdenUseCase.ejecutar(404L)).thenThrow(new OrdenNoEncontradaException(404L));

        mockMvc.perform(get("/api/v1/orden/404"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarEstadoConTransicionInvalidaDevuelve422() throws Exception {
        when(actualizarEstadoOrdenUseCase.ejecutar(any()))
                .thenThrow(new TransicionEstadoInvalidaException(EstadoOrden.CREADA, EstadoOrden.COMPLETADA));

        String body = """
                {"estadoNuevo":"COMPLETADA","version":0,"usuario":"tester","motivo":"salto invalido"}
                """;

        mockMvc.perform(put("/api/v1/orden/1/estado").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void actualizarEstadoConVersionObsoletaDevuelve409() throws Exception {
        when(actualizarEstadoOrdenUseCase.ejecutar(any()))
                .thenThrow(new ConflictoConcurrenciaException(1L, 0L, 1L));

        String body = """
                {"estadoNuevo":"ASIGNADA","version":0,"usuario":"tester","motivo":"motivo"}
                """;

        mockMvc.perform(put("/api/v1/orden/1/estado").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void listarOrdenesDevuelvePaginaVacia() throws Exception {
        when(listarOrdenesUseCase.ejecutar(any(), anyInt(), anyInt()))
                .thenReturn(new PaginaResultado<>(java.util.List.of(), 0, 20, 0, 0));

        mockMvc.perform(get("/api/v1/orden"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos").value(0));
    }
}
