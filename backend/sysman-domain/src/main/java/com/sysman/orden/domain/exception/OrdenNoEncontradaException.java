package com.sysman.orden.domain.exception;

public class OrdenNoEncontradaException extends DominioException {

    private final Long ordenId;

    public OrdenNoEncontradaException(Long ordenId) {
        super("Orden no encontrada: " + ordenId);
        this.ordenId = ordenId;
    }

    public Long getOrdenId() {
        return ordenId;
    }
}
