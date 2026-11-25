package com.dynamika.buildwise.evaluation.controller;

import com.dynamika.buildwise.evaluation.dto.WorkerScoreResponse;
import com.dynamika.buildwise.evaluation.service.IScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Lecture seule : la note est recalculée par le service à chaque avis créé, modifié ou supprimé.
@RestController
@RequestMapping("/scores")
@Tag(name = "Scores", description = "La note moyenne de chaque ouvrier")
public class ScoreController {
    private static final Logger log = LoggerFactory.getLogger(ScoreController.class);

    private final IScoreService service;
    private final int port;

    public ScoreController(IScoreService service, @Value("${server.port}") int port) {
        this.service = service;
        this.port = port;
    }

    @GetMapping
    @Operation(summary = "Lister les notes des ouvriers")
    public List<WorkerScoreResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{workerId}")
    @Operation(summary = "Lire la note d'un ouvrier", description = "Sans avis : moyenne 0 et nombre 0.")
    public WorkerScoreResponse findByWorkerId(@PathVariable UUID workerId) {
        log.info("requête reçue sur le port {}", port);
        return service.findByWorkerId(workerId);
    }
}
