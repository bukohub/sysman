package com.sysman.orden.application.service;

import com.sysman.orden.application.port.in.SugerirUsuariosUseCase;
import com.sysman.orden.application.port.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SugerirUsuariosService implements SugerirUsuariosUseCase {

    private static final int LIMITE_MAXIMO = 20;

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public SugerirUsuariosService(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> ejecutar(String texto, int limite) {
        String textoBusqueda = texto == null ? "" : texto.trim();
        int limiteEfectivo = Math.min(Math.max(limite, 1), LIMITE_MAXIMO);
        return usuarioRepositoryPort.buscarSugerencias(textoBusqueda, limiteEfectivo);
    }
}
