package com.sysman.orden.infrastructure.persistence.mapper;

import com.sysman.orden.domain.model.Cliente;
import com.sysman.orden.domain.model.Orden;
import com.sysman.orden.domain.model.OrdenHistorico;
import com.sysman.orden.infrastructure.persistence.entity.ClienteEntity;
import com.sysman.orden.infrastructure.persistence.entity.OrdenEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrdenEntityMapper {

    public Orden toDomain(OrdenEntity entity, Cliente cliente, List<OrdenHistorico> historico) {
        return new Orden(
                entity.getId(),
                cliente,
                entity.getTipoOrden(),
                entity.getEstado(),
                entity.getDescripcion(),
                entity.getDireccionServicio(),
                entity.getCreadoPor(),
                entity.getFechaCreacion(),
                entity.getModificadoPor(),
                entity.getFechaModificacion(),
                entity.getVersion(),
                historico
        );
    }

    public OrdenEntity toNewEntity(Orden orden, ClienteEntity clienteRef) {
        OrdenEntity entity = new OrdenEntity();
        entity.setCliente(clienteRef);
        entity.setTipoOrden(orden.getTipoOrden());
        entity.setEstado(orden.getEstado());
        entity.setDescripcion(orden.getDescripcion());
        entity.setDireccionServicio(orden.getDireccionServicio());
        entity.setCreadoPor(orden.getCreadoPor());
        entity.setFechaCreacion(orden.getFechaCreacion() != null ? orden.getFechaCreacion() : LocalDateTime.now());
        return entity;
    }
}
