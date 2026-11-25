package com.dynamika.buildwise.budget.controller;

import com.dynamika.buildwise.budget.dto.BudgetLineRequest;
import com.dynamika.buildwise.budget.dto.BudgetLineResponse;
import com.dynamika.buildwise.budget.dto.ProjectBudget;
import com.dynamika.buildwise.budget.service.IBudgetLineService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Budget lines", description = "Les lignes de budget prévues par chantier, et le bilan du chantier")
public class BudgetLineController {
    private static final Logger log = LoggerFactory.getLogger(BudgetLineController.class);

    private final IBudgetLineService service;
    private final int port;

    public BudgetLineController(IBudgetLineService service, @Value("${server.port}") int port) {
        this.service = service;
        this.port = port;
    }

    @GetMapping("/budget-lines")
    @Operation(summary = "Lister les lignes de budget", description = "Avec projectId : celles d'un seul chantier.")
    public List<BudgetLineResponse> findAll(@RequestParam(required = false) UUID projectId) {
        log.info("requête reçue sur le port {}", port);   // pour voir la Gateway alterner entre deux instances
        return service.findAll(projectId);
    }

    @GetMapping("/budget-lines/{id}")
    @Operation(summary = "Lire une ligne de budget")
    public BudgetLineResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping("/budget-lines")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une ligne de budget")
    public BudgetLineResponse create(@Valid @RequestBody BudgetLineRequest request) {
        return service.create(request);
    }

    @PutMapping("/budget-lines/{id}")
    @Operation(summary = "Modifier une ligne de budget")
    public BudgetLineResponse update(@PathVariable UUID id, @Valid @RequestBody BudgetLineRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/budget-lines/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une ligne de budget", description = "Supprime aussi ses coûts.")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    // Seul /projects/*/budget (et /costs) arrive ici : la Gateway envoie le reste de /projects/** à planning.
    @GetMapping("/projects/{id}/budget")
    @Operation(summary = "Bilan du budget d'un chantier", description = "total prévu, dépensé et restant ; 0 si vide.")
    public ProjectBudget budget(@PathVariable UUID id) {
        log.info("requête reçue sur le port {}", port);
        return service.budget(id);
    }
}
