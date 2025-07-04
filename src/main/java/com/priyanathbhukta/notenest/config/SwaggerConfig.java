package com.priyanathbhukta.notenest.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	
	
	@Bean
    public OpenAPI openAPI() {
        OpenAPI openApi = new OpenAPI();

        Info info = new Info();
        info.setTitle("NoteNest API");
        info.setDescription("NoteNest Api");
        info.setVersion("1.0.0");
        info.setTermsOfService("http://note_nest.com");
        info.setContact(
            new Contact()
                .email("bhuktapriyanath@gmail.com")
                .name("Priyanath Bhukta")
                .url("http://note_nest.com")
        );
        info.setLicense(
            new License()
                .name("NoteNest 1.0")
                .url("http://note_nest.com")
        );

        // Add server information
        List<Server> serverList = List.of(new Server()
            .description("Dev")
            .url("http://localhost:8080"),
        new Server()
        		.description("Test")
        		.url("http://localhost:8081"),
        new Server()
        		.description("Prod")
        		.url("http://localhost:8082"));
        
        SecurityScheme securityScheme = new SecurityScheme().name("Authrization")
        		.scheme("bearer")
        		.type(Type.HTTP)
        		.bearerFormat("JWT")
        		.in(In.HEADER);
        
        Components components = new Components().addSecuritySchemes("Token", securityScheme);
        openApi.setComponents(components);
        openApi.setServers(serverList);
        openApi.setInfo(info);
        openApi.setSecurity(List.of(new SecurityRequirement().addList("Token")));
        
        return openApi;
    }
}
