package com.dynamika.buildwise.materials.controller;

import com.dynamika.buildwise.materials.dto.SiteStockRequest;
import com.dynamika.buildwise.materials.dto.SiteStockResponse;
import com.dynamika.buildwise.materials.service.ISiteStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// Deux entrées : sous le matériau (/materials/{materialId}/site-stocks) et par chantier (/projects/{projectId}/material-stock).
@RestController
@Tag(name = "Site stocks", description = "Le stock de chaque matériau sur chaque chantier")
public class SiteStockController {
    private final ISiteStockService service;

    public SiteStockController(ISiteStockService service) {
        this.service = service;
    }

    @GetMapping("/materials/{materialId}/site-stocks")
    @Operation(summary = "Lister le stock d'un matériau sur les chantiers", description = "404 si le matériau n'existe pas.")
    public List<SiteStockResponse> findAll(@PathVariable UUID materialId) {
        return service.findAll(materialId);
    }

    @GetMapping("/materials/{materialId}/site-stocks/{id}")
    @Operation(summary = "Lire une ligne de stock")
    public SiteStockResponse findById(@PathVariable UUID materialId, @PathVariable UUID id) {
        return service.findById(materialId, id);
    }

    @PostMapping("/materials/{materialId}/site-stocks")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une ligne de stock",
            description = "404 si le matériau n'existe pas, 409 si le chantier a déjà une ligne pour ce matériau.")
    public SiteStockResponse create(@PathVariable UUID materialId, @Valid @RequestBody SiteStockRequest request) {
        return service.create(materialId, request);
    }

    @PutMapping("/materials/{materialId}/site-stocks/{id}")
    @Operation(summary = "Modifier une ligne de stock")
    public SiteStockResponse update(@PathVariable UUID materialId, @PathVariable UUID id,
                                    @Valid @RequestBody SiteStockRequest request) {
        return service.update(materialId, id, request);
    }

    @DeleteMapping("/materials/{materialId}/site-stocks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une ligne de stock", description = "409 si des signalements de pénurie y sont liés.")
    public void delete(@PathVariable UUID materialId, @PathVariable UUID id) {
        service.delete(materialId, id);
    }

    @GetMapping("/projects/{projectId}/material-stock")
    @Operation(summary = "Lister le stock d'un chantier", description = "Toutes les lignes de stock du projet.")
    public List<SiteStockResponse> findByProject(@PathVariable UUID projectId) {
        return service.findByProject(projectId);
    }
}
