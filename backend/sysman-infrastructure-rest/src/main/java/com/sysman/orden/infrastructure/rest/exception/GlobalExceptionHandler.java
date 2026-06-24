package com.sysman.orden.infrastructure.rest.exception;

import com.sysman.orden.domain.exception.ClienteNoEncontradoException;
import com.sysman.orden.domain.exception.ConflictoConcurrenciaException;
import com.sysman.orden.domain.exception.OrdenNoEncontradaException;
import com.sysman.orden.domain.exception.TransicionEstadoInvalidaException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(OrdenNoEncontradaException.class)
    public ProblemDetail manejarOrdenNoEncontrada(OrdenNoEncontradaException ex, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, "Orden no encontrada", ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ProblemDetail manejarClienteNoEncontrado(ClienteNoEncontradoException ex, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, "Cliente no encontrado", ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler(TransicionEstadoInvalidaException.class)
    public ProblemDetail manejarTransicionInvalida(TransicionEstadoInvalidaException ex, HttpServletRequest request) {
        Map<String, Object> propiedades = Map.of(
                "estadoActual", ex.getEstadoActual() == null ? "" : ex.getEstadoActual().name(),
                "estadoSolicitado", ex.getEstadoSolicitado().name());
        return construir(HttpStatus.UNPROCESSABLE_ENTITY, "Transicion de estado invalida", ex.getMessage(), request, propiedades);
    }

    @ExceptionHandler(ConflictoConcurrenciaException.class)
    public ProblemDetail manejarConflictoConcurrencia(ConflictoConcurrenciaException ex, HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT, "Conflicto de concurrencia", ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail manejarValidacion(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Map<String, String>> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(this::aMapaError)
                .collect(Collectors.toList());
        return construir(HttpStatus.BAD_REQUEST, "Error de validacion", "La solicitud contiene campos invalidos",
                request, Map.of("errores", errores));
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail manejarGenerico(Exception ex, HttpServletRequest request) {
        String correlationId = org.slf4j.MDC.get("correlationId");
        log.error("Error no controlado [correlationId={}]", correlationId, ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno",
                "Ocurrio un error inesperado. Contacte al administrador con el id de correlacion.", request, Map.of());
    }

    private Map<String, String> aMapaError(FieldError fieldError) {
        return Map.of("campo", fieldError.getField(), "mensaje", fieldError.getDefaultMessage());
    }

    private ProblemDetail construir(HttpStatus status, String titulo, String detalle,
                                     HttpServletRequest request, Map<String, Object> propiedadesExtra) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detalle);
        problemDetail.setTitle(titulo);
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));
        problemDetail.setProperty("correlationId", org.slf4j.MDC.get("correlationId"));
        problemDetail.setProperty("timestamp", java.time.LocalDateTime.now().toString());
        propiedadesExtra.forEach(problemDetail::setProperty);
        return problemDetail;
    }
}
