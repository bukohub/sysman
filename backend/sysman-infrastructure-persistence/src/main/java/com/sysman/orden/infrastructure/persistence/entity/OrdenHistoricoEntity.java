package com.sysman.orden.infrastructure.persistence.entity;

import com.sysman.orden.domain.model.EstadoOrden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "orden_historico")
public class OrdenHistoricoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orden_id", nullable = false)
    private Long ordenId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior")
    private EstadoOrden estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false)
    private EstadoOrden estadoNuevo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @Column(name = "usuario", nullable = false)
    private String usuario;

    @Column(name = "motivo")
    private String motivo;

    public OrdenHistoricoEntity() {
    }

    public Long getId() {
        return id;
    }

    public Long getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(Long ordenId) {
        this.ordenId = ordenId;
    }

    public EstadoOrden getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(EstadoOrden estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public EstadoOrden getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(EstadoOrden estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
