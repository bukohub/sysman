package com.sysman.orden.infrastructure.persistence.specification;

import com.sysman.orden.domain.model.EstadoOrden;
import com.sysman.orden.infrastructure.persistence.entity.OrdenEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class OrdenSpecifications {

    private OrdenSpecifications() {
    }

    public static Specification<OrdenEntity> conEstado(EstadoOrden estado) {
        return (root, query, builder) ->
                estado == null ? null : builder.equal(root.get("estado"), estado);
    }

    public static Specification<OrdenEntity> conFechaCreacionDesde(LocalDateTime fechaInicio) {
        return (root, query, builder) ->
                fechaInicio == null ? null : builder.greaterThanOrEqualTo(root.get("fechaCreacion"), fechaInicio);
    }

    public static Specification<OrdenEntity> conFechaCreacionHasta(LocalDateTime fechaFin) {
        return (root, query, builder) ->
                fechaFin == null ? null : builder.lessThanOrEqualTo(root.get("fechaCreacion"), fechaFin);
    }
}
