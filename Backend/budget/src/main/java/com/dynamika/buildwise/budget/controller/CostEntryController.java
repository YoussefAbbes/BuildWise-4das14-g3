package com.dynamika.buildwise.budget.controller;

import com.dynamika.buildwise.budget.dto.CostEntryRequest;
import com.dynamika.buildwise.budget.dto.CostEntryResponse;
import com.dynamika.buildwise.budget.service.ICostEntryService;
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

@RestController
@Tag(name = "Cost entries", description = "Les coûts réels imputés à une ligne de budget")
public class CostEntryController {
    private final ICostEntryService service;

    public CostEntryController(ICostEntryService service) {
        this.service = service;
    }

    @GetMapping("/budget-lines/{lineId}/costs")
    @Operation(summary = "Lister les coûts d'une ligne", description = "404 si la ligne n'existe pas.")
    public List<CostEntryResponse> findAll(@PathVariable UUID lineId) {
        return service.findAll(lineId);
    }

    @GetMapping("/budget-lines/{lineId}/costs/{id}")
    @Operation(summary = "Lire un coût", description = "404 si le coût n'est pas dans cette ligne.")
    public CostEntryResponse findById(@PathVariable UUID lineId, @PathVariable UUID id) {
        return service.findById(lineId, id);
    }

    @PostMapping("/budget-lines/{lineId}/costs")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter un coût à une ligne", description = "404 si la ligne n'existe pas, 409 si l'eventId est déjà utilisé.")
    public CostEntryResponse create(@PathVariable UUID lineId, @Valid @RequestBody CostEntryRequest request) {
        return service.create(lineId, request);
    }

    @PutMapping("/budget-lines/{lineId}/costs/{id}")
    @Operation(summary = "Modifier un coût", description = "409 si l'eventId est déjà pris par un autre coût.")
    public CostEntryResponse update(@PathVariable UUID lineId, @PathVariable UUID id,
                                    @Valid @RequestBody CostEntryRequest request) {
        return service.update(lineId, id, request);
    }

    @DeleteMapping("/budget-lines/{lineId}/costs/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un coût")
    public void delete(@PathVariable UUID lineId, @PathVariable UUID id) {
        service.delete(lineId, id);
    }

    @GetMapping("/projects/{id}/costs")
    @Operation(summary = "Lister les coûts d'un chantier", description = "Toutes ses lignes confondues.")
    public List<CostEntryResponse> findByProject(@PathVariable UUID id) {
        return service.findByProject(id);
    }
}
