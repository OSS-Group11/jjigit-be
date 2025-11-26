package com.jigit.backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI configuration
 * Provides API documentation and testing interface
 */
@Configuration
public class SwaggerConfig {

    @Value("${prod.server.url:http://localhost:8080}")
    private String prodServerUrl;

    /**
     * OpenAPI configuration bean
     * Defines API metadata, JWT authentication scheme, and server URLs
     */
    @Bean
    public OpenAPI openAPI() {
        // Security scheme name
        String jwtSchemeName = "JWT Token";

        // Security requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(jwtSchemeName);

        // Security scheme configuration
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter JWT token"));

        // Server configurations
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local development server");

        Server prodServer = new Server()
                .url(prodServerUrl)
                .description("Production server");

        return new OpenAPI()
                .info(new Info()
                        .title("JJiGiT API")
                        .description("JJiGiT Voting Service REST API Documentation")
                        .version("v1.0.0"))
                .servers(List.of(localServer, prodServer))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
