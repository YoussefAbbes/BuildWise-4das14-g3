package com.dynamika.buildwise.materials.controller;

import com.dynamika.buildwise.materials.dto.MaterialRequest;
import com.dynamika.buildwise.materials.dto.MaterialResponse;
import com.dynamika.buildwise.materials.service.IMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/materials")
@Tag(name = "Materials", description = "Le catalogue des matériaux")
public class MaterialController {
    private static final Logger log = LoggerFactory.getLogger(MaterialController.class);

    private final IMaterialService service;
    private final int port;

    public MaterialController(IMaterialService service, @Value("${server.port}") int port) {
        this.service = service;
        this.port = port;
    }

    @GetMapping
    @Operation(summary = "Lister les matériaux")
    public List<MaterialResponse> findAll() {
        log.info("requête reçue sur le port {}", port);   // pour voir la Gateway alterner entre deux instances
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lire un matériau", description = "404 si le matériau n'existe pas.")
    public MaterialResponse findById(@PathVariable UUID id) {
        log.info("requête reçue sur le port {}", port);
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un matériau")
    public MaterialResponse create(@Valid @RequestBody MaterialRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un matériau")
    public MaterialResponse update(@PathVariable UUID id, @Valid @RequestBody MaterialRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un matériau", description = "409 s'il a encore des stocks ou des commandes.")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
