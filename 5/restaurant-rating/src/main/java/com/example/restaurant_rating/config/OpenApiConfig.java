package com.example.restaurant_rating.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Restaurant Rating API", version = "1.0", description = "API for managing restaurant ratings"))
public class OpenApiConfig {
}