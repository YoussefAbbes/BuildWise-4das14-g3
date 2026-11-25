package com.dynamika.buildwise.workforce;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "BuildWise - workforce", version = "1.0",
        description = "Ouvriers, affectations et pointages"))
public class WorkforceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkforceApplication.class, args);
    }
}
