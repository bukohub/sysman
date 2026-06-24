package com.sysman.orden.application.port.in;

import com.sysman.orden.domain.model.Cliente;

import java.util.List;

public interface BuscarClientesUseCase {
    List<Cliente> ejecutar(String texto, int limite);
}
