package com.sysman.orden.infrastructure.rest.v1.mapper;

import com.sysman.orden.application.command.ActualizarEstadoOrdenCommand;
import com.sysman.orden.application.command.CrearOrdenCommand;
import com.sysman.orden.domain.model.Orden;
import com.sysman.orden.domain.model.OrdenHistorico;
import com.sysman.orden.infrastructure.rest.v1.dto.request.ActualizarEstadoOrdenRequest;
import com.sysman.orden.infrastructure.rest.v1.dto.request.CrearOrdenRequest;
import com.sysman.orden.infrastructure.rest.v1.dto.response.ClienteResumenResponse;
import com.sysman.orden.infrastructure.rest.v1.dto.response.OrdenHistoricoResponse;
import com.sysman.orden.infrastructure.rest.v1.dto.response.OrdenResponse;
import com.sysman.orden.infrastructure.rest.v1.dto.response.OrdenResumenResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdenRestMapper {

    public CrearOrdenCommand toCommand(CrearOrdenRequest request) {
        return new CrearOrdenCommand(request.clienteId(), request.tipoOrden(), request.descripcion(),
                request.direccionServicio(), request.usuario());
    }

    public ActualizarEstadoOrdenCommand toCommand(Long ordenId, ActualizarEstadoOrdenRequest request) {
        return new ActualizarEstadoOrdenCommand(ordenId, request.estadoNuevo(), request.version(),
                request.usuario(), request.motivo());
    }

    public OrdenResponse toResponse(Orden orden) {
        ClienteResumenResponse cliente = new ClienteResumenResponse(
                orden.getCliente().getId(), orden.getCliente().getNumeroIdentificacion(), orden.getCliente().getNombre());

        List<OrdenHistoricoResponse> historico = orden.getHistorico().stream()
                .map(this::toHistoricoResponse)
                .collect(Collectors.toList());

        return new OrdenResponse(orden.getId(), cliente, orden.getTipoOrden(), orden.getEstado(),
                orden.getDescripcion(), orden.getDireccionServicio(), orden.getFechaCreacion(),
                orden.getFechaModificacion(), orden.getVersion(), historico);
    }

    public OrdenResumenResponse toResumenResponse(Orden orden) {
        return new OrdenResumenResponse(orden.getId(), orden.getCliente().getId(), orden.getCliente().getNombre(),
                orden.getTipoOrden(), orden.getEstado(), orden.getFechaCreacion());
    }

    private OrdenHistoricoResponse toHistoricoResponse(OrdenHistorico historico) {
        return new OrdenHistoricoResponse(historico.getId(), historico.getEstadoAnterior(), historico.getEstadoNuevo(),
                historico.getFechaCambio(), historico.getUsuario(), historico.getMotivo());
    }
}
