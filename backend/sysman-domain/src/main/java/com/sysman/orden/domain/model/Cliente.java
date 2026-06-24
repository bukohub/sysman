package com.sysman.orden.domain.model;

import java.time.LocalDateTime;

public class Cliente {

    private final Long id;
    private final String numeroIdentificacion;
    private final String nombre;
    private final String direccion;
    private final String telefono;
    private final String email;
    private final LocalDateTime fechaCreacion;

    public Cliente(Long id, String numeroIdentificacion, String nombre, String direccion,
                    String telefono, String email, LocalDateTime fechaCreacion) {
        this.id = id;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
