package com.sysman.orden.infrastructure.persistence.mapper;

import com.sysman.orden.domain.model.OrdenHistorico;
import com.sysman.orden.infrastructure.persistence.entity.OrdenHistoricoEntity;
import org.springframework.stereotype.Component;

@Component
public class OrdenHistoricoEntityMapper {

    public OrdenHistorico toDomain(OrdenHistoricoEntity entity) {
        return new OrdenHistorico(
                entity.getId(),
                entity.getOrdenId(),
                entity.getEstadoAnterior(),
                entity.getEstadoNuevo(),
                entity.getFechaCambio(),
                entity.getUsuario(),
                entity.getMotivo()
        );
    }

    public OrdenHistoricoEntity toNewEntity(OrdenHistorico domain, Long ordenId) {
        OrdenHistoricoEntity entity = new OrdenHistoricoEntity();
        entity.setOrdenId(ordenId);
        entity.setEstadoAnterior(domain.getEstadoAnterior());
        entity.setEstadoNuevo(domain.getEstadoNuevo());
        entity.setFechaCambio(domain.getFechaCambio());
        entity.setUsuario(domain.getUsuario());
        entity.setMotivo(domain.getMotivo());
        return entity;
    }
}
