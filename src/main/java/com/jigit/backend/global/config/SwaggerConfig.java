package com.jigit.backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration
 * Provides API documentation and testing interface
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI configuration bean
     * Defines API metadata and JWT authentication scheme
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

        return new OpenAPI()
                .info(new Info()
                        .title("JJiGiT API")
                        .description("JJiGiT Voting Service REST API Documentation")
                        .version("v1.0.0"))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
