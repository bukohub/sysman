package com.sysman.orden.domain.exception;

import com.sysman.orden.domain.model.EstadoOrden;

public class TransicionEstadoInvalidaException extends DominioException {

    private final EstadoOrden estadoActual;
    private final EstadoOrden estadoSolicitado;

    public TransicionEstadoInvalidaException(EstadoOrden estadoActual, EstadoOrden estadoSolicitado) {
        super("Transicion invalida de " + estadoActual + " a " + estadoSolicitado);
        this.estadoActual = estadoActual;
        this.estadoSolicitado = estadoSolicitado;
    }

    public EstadoOrden getEstadoActual() {
        return estadoActual;
    }

    public EstadoOrden getEstadoSolicitado() {
        return estadoSolicitado;
    }
}
