package com.sysman.orden.domain.model;

import java.time.LocalDateTime;

public class OrdenHistorico {

    private final Long id;
    private final Long ordenId;
    private final EstadoOrden estadoAnterior;
    private final EstadoOrden estadoNuevo;
    private final LocalDateTime fechaCambio;
    private final String usuario;
    private final String motivo;

    public OrdenHistorico(Long id, Long ordenId, EstadoOrden estadoAnterior, EstadoOrden estadoNuevo,
                           LocalDateTime fechaCambio, String usuario, String motivo) {
        this.id = id;
        this.ordenId = ordenId;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
        this.usuario = usuario;
        this.motivo = motivo;
    }

    public static OrdenHistorico de(Long ordenId, EstadoOrden estadoAnterior, EstadoOrden estadoNuevo,
                                     String usuario, String motivo) {
        return new OrdenHistorico(null, ordenId, estadoAnterior, estadoNuevo, LocalDateTime.now(), usuario, motivo);
    }

    public Long getId() {
        return id;
    }

    public Long getOrdenId() {
        return ordenId;
    }

    public EstadoOrden getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoOrden getEstadoNuevo() {
        return estadoNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getMotivo() {
        return motivo;
    }
}
