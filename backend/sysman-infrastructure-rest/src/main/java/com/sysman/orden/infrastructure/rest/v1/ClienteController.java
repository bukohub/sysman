package com.sysman.orden.infrastructure.rest.v1;

import com.sysman.orden.application.port.in.BuscarClientesUseCase;
import com.sysman.orden.domain.model.Cliente;
import com.sysman.orden.infrastructure.rest.v1.dto.response.ClienteResumenResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cliente")
public class ClienteController {

    private final BuscarClientesUseCase buscarClientesUseCase;

    public ClienteController(BuscarClientesUseCase buscarClientesUseCase) {
        this.buscarClientesUseCase = buscarClientesUseCase;
    }

    @GetMapping
    public List<ClienteResumenResponse> buscarClientes(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(defaultValue = "10") int limit) {
        return buscarClientesUseCase.ejecutar(q, limit).stream()
                .map(this::toResumenResponse)
                .toList();
    }

    private ClienteResumenResponse toResumenResponse(Cliente cliente) {
        return new ClienteResumenResponse(cliente.getId(), cliente.getNumeroIdentificacion(), cliente.getNombre());
    }
}
