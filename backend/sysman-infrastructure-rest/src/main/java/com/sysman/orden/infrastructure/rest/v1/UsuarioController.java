package com.sysman.orden.infrastructure.rest.v1;

import com.sysman.orden.application.port.in.SugerirUsuariosUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    private final SugerirUsuariosUseCase sugerirUsuariosUseCase;

    public UsuarioController(SugerirUsuariosUseCase sugerirUsuariosUseCase) {
        this.sugerirUsuariosUseCase = sugerirUsuariosUseCase;
    }

    @GetMapping("/sugerencias")
    public List<String> sugerencias(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(defaultValue = "10") int limit) {
        return sugerirUsuariosUseCase.ejecutar(q, limit);
    }
}
