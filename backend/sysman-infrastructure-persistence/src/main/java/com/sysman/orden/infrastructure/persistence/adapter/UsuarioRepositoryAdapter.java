package com.sysman.orden.infrastructure.persistence.adapter;

import com.sysman.orden.application.port.out.UsuarioRepositoryPort;
import com.sysman.orden.infrastructure.persistence.repository.OrdenHistoricoJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final OrdenHistoricoJpaRepository ordenHistoricoJpaRepository;

    public UsuarioRepositoryAdapter(OrdenHistoricoJpaRepository ordenHistoricoJpaRepository) {
        this.ordenHistoricoJpaRepository = ordenHistoricoJpaRepository;
    }

    @Override
    public List<String> buscarSugerencias(String texto, int limite) {
        return ordenHistoricoJpaRepository.buscarUsuariosPorTexto(texto, PageRequest.of(0, limite));
    }
}
