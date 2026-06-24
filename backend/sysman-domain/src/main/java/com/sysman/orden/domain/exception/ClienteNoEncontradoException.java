package com.sysman.orden.domain.exception;

public class ClienteNoEncontradoException extends DominioException {

    private final Long clienteId;

    public ClienteNoEncontradoException(Long clienteId) {
        super("Cliente no encontrado: " + clienteId);
        this.clienteId = clienteId;
    }

    public Long getClienteId() {
        return clienteId;
    }
}
