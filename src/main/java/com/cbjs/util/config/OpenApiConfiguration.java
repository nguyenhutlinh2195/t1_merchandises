package com.cbjs.util.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Administrator",
                        email = "admin@t1store.com",
                        url = "http://localhost:8081/api/swagger-ui/index.html"
                ),
                description = "OpenAPI documentation for Spring Security",
                title = "OpenAPI specification - Spring T1 Merchandises Store",
                version = "1.0",
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8081/api",
                        description = "Local ENV"
                )
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {
}
