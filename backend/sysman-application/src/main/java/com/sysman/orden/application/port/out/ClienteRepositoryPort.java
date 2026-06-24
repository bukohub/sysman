package com.sysman.orden.application.port.out;

import com.sysman.orden.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {
    Optional<Cliente> buscarPorId(Long id);

    List<Cliente> buscarPorTexto(String texto, int limite);
}
