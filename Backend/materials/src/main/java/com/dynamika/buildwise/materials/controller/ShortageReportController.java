package com.dynamika.buildwise.materials.controller;

import com.dynamika.buildwise.materials.dto.ShortageReportRequest;
import com.dynamika.buildwise.materials.dto.ShortageReportResponse;
import com.dynamika.buildwise.materials.service.IShortageReportService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material-shortages")
@Tag(name = "Material shortages", description = "Les signalements de pénurie sur une ligne de stock")
public class ShortageReportController {
    private final IShortageReportService service;

    public ShortageReportController(IShortageReportService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lister les signalements", description = "Avec siteStockId : ceux d'une seule ligne de stock.")
    public List<ShortageReportResponse> findAll(@RequestParam(required = false) UUID siteStockId) {
        return service.findAll(siteStockId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lire un signalement")
    public ShortageReportResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Signaler une pénurie", description = "404 si la ligne de stock n'existe pas.")
    public ShortageReportResponse create(@Valid @RequestBody ShortageReportRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un signalement", description = "La date du signalement ne change pas.")
    public ShortageReportResponse update(@PathVariable UUID id, @Valid @RequestBody ShortageReportRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un signalement")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
