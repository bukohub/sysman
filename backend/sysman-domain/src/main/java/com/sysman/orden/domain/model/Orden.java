package com.sysman.orden.domain.model;

import com.sysman.orden.domain.service.ValidadorTransicionEstado;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Orden {

    private final Long id;
    private final Cliente cliente;
    private final TipoOrden tipoOrden;
    private EstadoOrden estado;
    private final String descripcion;
    private final String direccionServicio;
    private final String creadoPor;
    private final LocalDateTime fechaCreacion;
    private String modificadoPor;
    private LocalDateTime fechaModificacion;
    private Long version;
    private final List<OrdenHistorico> historico;

    public Orden(Long id, Cliente cliente, TipoOrden tipoOrden, EstadoOrden estado,
                 String descripcion, String direccionServicio, String creadoPor,
                 LocalDateTime fechaCreacion, String modificadoPor, LocalDateTime fechaModificacion,
                 Long version, List<OrdenHistorico> historico) {
        this.id = id;
        this.cliente = cliente;
        this.tipoOrden = tipoOrden;
        this.estado = estado;
        this.descripcion = descripcion;
        this.direccionServicio = direccionServicio;
        this.creadoPor = creadoPor;
        this.fechaCreacion = fechaCreacion;
        this.modificadoPor = modificadoPor;
        this.fechaModificacion = fechaModificacion;
        this.version = version;
        this.historico = new ArrayList<>(historico != null ? historico : List.of());
    }

    public static Orden crear(Cliente cliente, TipoOrden tipoOrden, String descripcion,
                               String direccionServicio, String creadoPor) {
        Orden orden = new Orden(null, cliente, tipoOrden, EstadoOrden.CREADA, descripcion,
                direccionServicio, creadoPor, LocalDateTime.now(), null, null, 0L, new ArrayList<>());
        orden.historico.add(OrdenHistorico.de(null, null, EstadoOrden.CREADA, creadoPor, "Creacion inicial de la orden"));
        return orden;
    }

    public void cambiarEstado(EstadoOrden nuevoEstado, String usuario, String motivo) {
        ValidadorTransicionEstado.validar(this.estado, nuevoEstado);
        this.historico.add(OrdenHistorico.de(this.id, this.estado, nuevoEstado, usuario, motivo));
        this.estado = nuevoEstado;
        this.modificadoPor = usuario;
        this.fechaModificacion = LocalDateTime.now();
        this.version = this.version + 1;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public TipoOrden getTipoOrden() {
        return tipoOrden;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDireccionServicio() {
        return direccionServicio;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public Long getVersion() {
        return version;
    }

    public List<OrdenHistorico> getHistorico() {
        return List.copyOf(historico);
    }
}
