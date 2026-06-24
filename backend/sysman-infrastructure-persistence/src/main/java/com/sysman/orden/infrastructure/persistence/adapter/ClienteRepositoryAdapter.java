package com.sysman.orden.infrastructure.persistence.adapter;

import com.sysman.orden.application.port.out.ClienteRepositoryPort;
import com.sysman.orden.domain.model.Cliente;
import com.sysman.orden.infrastructure.persistence.mapper.ClienteEntityMapper;
import com.sysman.orden.infrastructure.persistence.repository.ClienteJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository clienteJpaRepository;
    private final ClienteEntityMapper clienteEntityMapper;

    public ClienteRepositoryAdapter(ClienteJpaRepository clienteJpaRepository, ClienteEntityMapper clienteEntityMapper) {
        this.clienteJpaRepository = clienteJpaRepository;
        this.clienteEntityMapper = clienteEntityMapper;
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteJpaRepository.findById(id).map(clienteEntityMapper::toDomain);
    }

    @Override
    public List<Cliente> buscarPorTexto(String texto, int limite) {
        return clienteJpaRepository.buscarPorTexto(texto, PageRequest.of(0, limite)).stream()
                .map(clienteEntityMapper::toDomain)
                .toList();
    }
}
