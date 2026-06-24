package com.sysman.orden.infrastructure.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Habilita CORS para los origenes configurados (ej. el frontend Next.js en
 * desarrollo local, que corre en un puerto distinto al de esta API).
 * Sin esto el navegador bloquea las peticiones del frontend por same-origin policy.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String[] allowedOrigins;

    public CorsConfig(@Value("${app.cors.allowed-origins:}") String allowedOrigins) {
        this.allowedOrigins = allowedOrigins.isBlank() ? new String[0] : allowedOrigins.split(",");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (allowedOrigins.length == 0) {
            return;
        }
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
