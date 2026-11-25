package com.dynamika.buildwise.workforce.controller;

import com.dynamika.buildwise.workforce.dto.AssignmentRequest;
import com.dynamika.buildwise.workforce.dto.AssignmentResponse;
import com.dynamika.buildwise.workforce.service.IAssignmentService;
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
@RequestMapping("/assignments")
@Tag(name = "Assignments", description = "Les affectations d'un ouvrier à une tâche")
public class AssignmentController {
    private final IAssignmentService service;

    public AssignmentController(IAssignmentService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lister les affectations", description = "Avec workerId : celles d'un seul ouvrier.")
    public List<AssignmentResponse> findAll(@RequestParam(required = false) UUID workerId) {
        return service.findAll(workerId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lire une affectation")
    public AssignmentResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Affecter un ouvrier à une tâche", description = "404 si l'ouvrier n'existe pas.")
    public AssignmentResponse create(@Valid @RequestBody AssignmentRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une affectation")
    public AssignmentResponse update(@PathVariable UUID id, @Valid @RequestBody AssignmentRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une affectation")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
