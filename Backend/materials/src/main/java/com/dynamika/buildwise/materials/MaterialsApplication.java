package com.dynamika.buildwise.materials;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "BuildWise - materials", version = "1.0",
        description = "Catalogue des matériaux, stocks de chantier, pénuries et commandes"))
public class MaterialsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaterialsApplication.class, args);
    }
}

