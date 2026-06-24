package com.sysman.orden.infrastructure.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sysmanOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Sysman - Gestion de Ordenes Operativas")
                .version("v1")
                .description("API REST para la gestion de ordenes operativas de una empresa de servicios publicos"));
    }
}
