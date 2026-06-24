package com.sysman.orden.application.port.out;

import java.util.List;

public interface UsuarioRepositoryPort {
    List<String> buscarSugerencias(String texto, int limite);
}
