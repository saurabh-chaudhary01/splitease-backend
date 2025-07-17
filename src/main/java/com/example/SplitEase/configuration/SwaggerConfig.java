package com.example.SplitEase.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .info(new Info()
                        .title("SplitEase App")
                        .description("Backend APIs for the SplitEase expense-sharing app")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Saurabh Chaudhary")
                                .email("saurabh.ch169@gmail.com")
                                .url("https://github.com/saurabh-chaudhary01/splitease-backend"))
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8000/")
                                .description("splitease backend"))
                );
    }
}
