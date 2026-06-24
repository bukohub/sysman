package com.sysman.orden.application.port.in;

import java.util.List;

public interface SugerirUsuariosUseCase {
    List<String> ejecutar(String texto, int limite);
}
