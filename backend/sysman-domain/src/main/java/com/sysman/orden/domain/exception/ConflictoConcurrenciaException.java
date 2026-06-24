package com.sysman.orden.domain.exception;

public class ConflictoConcurrenciaException extends DominioException {

    private final Long ordenId;
    private final Long versionEsperada;
    private final Long versionActual;

    public ConflictoConcurrenciaException(Long ordenId, Long versionEsperada, Long versionActual) {
        super("Version obsoleta para orden " + ordenId
                + ": esperada=" + versionEsperada + " actual=" + versionActual);
        this.ordenId = ordenId;
        this.versionEsperada = versionEsperada;
        this.versionActual = versionActual;
    }

    public Long getOrdenId() {
        return ordenId;
    }

    public Long getVersionEsperada() {
        return versionEsperada;
    }

    public Long getVersionActual() {
        return versionActual;
    }
}
