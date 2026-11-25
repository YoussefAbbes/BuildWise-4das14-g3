package com.dynamika.buildwise.workforce.controller;

import com.dynamika.buildwise.workforce.dto.WorkerRequest;
import com.dynamika.buildwise.workforce.dto.WorkerResponse;
import com.dynamika.buildwise.workforce.service.IWorkerService;
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
@RequestMapping("/workers")
@Tag(name = "Workers", description = "Les ouvriers")
public class WorkerController {
    private static final Logger log = LoggerFactory.getLogger(WorkerController.class);

    private final IWorkerService service;
    private final int port;

    public WorkerController(IWorkerService service, @Value("${server.port}") int port) {
        this.service = service;
        this.port = port;
    }

    @GetMapping
    @Operation(summary = "Lister les ouvriers")
    public List<WorkerResponse> findAll() {
        log.info("requête reçue sur le port {}", port);
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lire un ouvrier", description = "404 si l'ouvrier n'existe pas.")
    public WorkerResponse findById(@PathVariable UUID id) {
        log.info("requête reçue sur le port {}", port);
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un ouvrier")
    public WorkerResponse create(@Valid @RequestBody WorkerRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un ouvrier")
    public WorkerResponse update(@PathVariable UUID id, @Valid @RequestBody WorkerRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un ouvrier", description = "409 s'il a encore des affectations ou des pointages.")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
