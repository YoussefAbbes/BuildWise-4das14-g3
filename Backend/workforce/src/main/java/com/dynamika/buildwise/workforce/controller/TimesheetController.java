package com.dynamika.buildwise.workforce.controller;

import com.dynamika.buildwise.workforce.dto.TimesheetRequest;
import com.dynamika.buildwise.workforce.dto.TimesheetResponse;
import com.dynamika.buildwise.workforce.service.ITimesheetService;
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
@RequestMapping("/timesheets")
@Tag(name = "Timesheets", description = "Les pointages (heures travaillées)")
public class TimesheetController {
    private final ITimesheetService service;

    public TimesheetController(ITimesheetService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lister les pointages", description = "Avec workerId : ceux d'un seul ouvrier.")
    public List<TimesheetResponse> findAll(@RequestParam(required = false) UUID workerId) {
        return service.findAll(workerId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lire un pointage")
    public TimesheetResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Pointer des heures", description = "404 si l'ouvrier n'existe pas.")
    public TimesheetResponse create(@Valid @RequestBody TimesheetRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un pointage")
    public TimesheetResponse update(@PathVariable UUID id, @Valid @RequestBody TimesheetRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un pointage")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
