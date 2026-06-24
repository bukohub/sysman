package com.sysman.orden.infrastructure.rest.v1;

import com.sysman.orden.application.port.in.ActualizarEstadoOrdenUseCase;
import com.sysman.orden.application.port.in.ConsultarOrdenUseCase;
import com.sysman.orden.application.port.in.CrearOrdenUseCase;
import com.sysman.orden.application.port.in.ListarOrdenesUseCase;
import com.sysman.orden.application.query.FiltroOrdenQuery;
import com.sysman.orden.application.query.PaginaResultado;
import com.sysman.orden.domain.model.EstadoOrden;
import com.sysman.orden.domain.model.Orden;
import com.sysman.orden.infrastructure.rest.v1.dto.request.ActualizarEstadoOrdenRequest;
import com.sysman.orden.infrastructure.rest.v1.dto.request.CrearOrdenRequest;
import com.sysman.orden.infrastructure.rest.v1.dto.response.OrdenResponse;
import com.sysman.orden.infrastructure.rest.v1.dto.response.OrdenResumenResponse;
import com.sysman.orden.infrastructure.rest.v1.dto.response.PageResponse;
import com.sysman.orden.infrastructure.rest.v1.mapper.OrdenRestMapper;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orden")
public class OrdenController {

    private final CrearOrdenUseCase crearOrdenUseCase;
    private final ConsultarOrdenUseCase consultarOrdenUseCase;
    private final ActualizarEstadoOrdenUseCase actualizarEstadoOrdenUseCase;
    private final ListarOrdenesUseCase listarOrdenesUseCase;
    private final OrdenRestMapper mapper;

    public OrdenController(CrearOrdenUseCase crearOrdenUseCase, ConsultarOrdenUseCase consultarOrdenUseCase,
                            ActualizarEstadoOrdenUseCase actualizarEstadoOrdenUseCase,
                            ListarOrdenesUseCase listarOrdenesUseCase, OrdenRestMapper mapper) {
        this.crearOrdenUseCase = crearOrdenUseCase;
        this.consultarOrdenUseCase = consultarOrdenUseCase;
        this.actualizarEstadoOrdenUseCase = actualizarEstadoOrdenUseCase;
        this.listarOrdenesUseCase = listarOrdenesUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<OrdenResponse> crearOrden(@Valid @RequestBody CrearOrdenRequest request) {
        Orden orden = crearOrdenUseCase.ejecutar(mapper.toCommand(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orden.getId())
                .toUri();
        return ResponseEntity.created(location).body(mapper.toResponse(orden));
    }

    @GetMapping("/{id}")
    public OrdenResponse consultarOrden(@PathVariable Long id) {
        return mapper.toResponse(consultarOrdenUseCase.ejecutar(id));
    }

    @PutMapping("/{id}/estado")
    public OrdenResponse actualizarEstado(@PathVariable Long id, @Valid @RequestBody ActualizarEstadoOrdenRequest request) {
        Orden orden = actualizarEstadoOrdenUseCase.ejecutar(mapper.toCommand(id, request));
        return mapper.toResponse(orden);
    }

    @GetMapping
    public PageResponse<OrdenResumenResponse> listarOrdenes(
            @RequestParam(required = false) EstadoOrden estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        FiltroOrdenQuery filtro = new FiltroOrdenQuery(estado, fechaInicio, fechaFin);
        PaginaResultado<Orden> resultado = listarOrdenesUseCase.ejecutar(filtro, page, size);

        List<OrdenResumenResponse> contenido = resultado.contenido().stream()
                .map(mapper::toResumenResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(contenido, resultado.pagina(), resultado.tamanioPagina(),
                resultado.totalElementos(), resultado.totalPaginas());
    }
}
