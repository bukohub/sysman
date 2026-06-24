package com.sysman.orden.infrastructure.persistence.mapper;

import com.sysman.orden.domain.model.Cliente;
import com.sysman.orden.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClienteEntityMapper {

    public Cliente toDomain(ClienteEntity entity) {
        return new Cliente(
                entity.getId(),
                entity.getNumeroIdentificacion(),
                entity.getNombre(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getEmail(),
                entity.getFechaCreacion()
        );
    }
}
