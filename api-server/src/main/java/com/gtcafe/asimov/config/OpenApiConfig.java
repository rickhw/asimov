package com.gtcafe.asimov.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Value("${asimov.openapi.title}")
    private String openapiTitle;

    @Value("${asimov.openapi.description}")
    private String description;

    @Value("${asimov.openapi.version}")
    private String openapiVersion;

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title(openapiTitle)
                        .description(description)
                        .version(openapiVersion))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("Development Server"),
                        new Server().url("http://rws.lab.gtcafe.com").description("Lab Server"),
                        new Server().url("http://rws.gtcafe.com").description("Production Server")
                ));

    }
}
