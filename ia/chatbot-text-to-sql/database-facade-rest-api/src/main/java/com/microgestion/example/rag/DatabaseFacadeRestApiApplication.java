package com.microgestion.example.rag;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Main class of the Spring Boot application for Database Facade REST API.
 * Configures the main components and API documentation.
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties
@ComponentScan(basePackages = "com.microgestion.example.rag")
public class DatabaseFacadeRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseFacadeRestApiApplication.class, args);
    }

    /**
     * Configures CORS for the application.
     * 
     * @return WebMvcConfigurer with the CORS configuration
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("POST")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }

    /**
     * Configures OpenAPI/Swagger for API documentation.
     * 
     * @return OpenAPI configuration for documentation
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Database Facade REST API")
                        .version("1.0.0")
                        .description("REST API for accessing relational databases using SQL queries")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    /**
     * Configures RestTemplate for HTTP calls.
     * 
     * @param builder RestTemplateBuilder injected by Spring
     * @return configured RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }
}