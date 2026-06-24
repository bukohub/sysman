package com.sysman.orden.infrastructure.persistence.entity;

import com.sysman.orden.domain.model.EstadoOrden;
import com.sysman.orden.domain.model.TipoOrden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.LocalDateTime;

@Entity
@Table(name = "orden")
public class OrdenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_orden", nullable = false)
    private TipoOrden tipoOrden;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoOrden estado;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "direccion_servicio")
    private String direccionServicio;

    @Column(name = "creado_por", nullable = false)
    private String creadoPor;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "modificado_por")
    private String modificadoPor;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public OrdenEntity() {
    }

    public Long getId() {
        return id;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public TipoOrden getTipoOrden() {
        return tipoOrden;
    }

    public void setTipoOrden(TipoOrden tipoOrden) {
        this.tipoOrden = tipoOrden;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccionServicio() {
        return direccionServicio;
    }

    public void setDireccionServicio(String direccionServicio) {
        this.direccionServicio = direccionServicio;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Long getVersion() {
        return version;
    }
}
