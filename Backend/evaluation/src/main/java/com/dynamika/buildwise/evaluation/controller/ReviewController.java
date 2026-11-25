package com.dynamika.buildwise.evaluation.controller;

import com.dynamika.buildwise.evaluation.dto.ReviewRequest;
import com.dynamika.buildwise.evaluation.dto.ReviewResponse;
import com.dynamika.buildwise.evaluation.dto.ReviewUpdateRequest;
import com.dynamika.buildwise.evaluation.service.IReviewService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Reviews", description = "Les avis sur le travail d'un ouvrier")
public class ReviewController {
    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    private final IReviewService service;
    private final int port;

    public ReviewController(IReviewService service, @Value("${server.port}") int port) {
        this.service = service;
        this.port = port;
    }

    @GetMapping
    @Operation(summary = "Lister les avis", description = "Avec workerId : ceux d'un seul ouvrier.")
    public List<ReviewResponse> findAll(@RequestParam(required = false) UUID workerId) {
        return service.findAll(workerId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lire un avis")
    public ReviewResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un avis",
            description = "Vérifie l'ouvrier auprès de workforce : 404 s'il n'existe pas, 503 si workforce est éteint,"
                    + " 409 s'il a déjà un avis pour cette tâche.")
    public ReviewResponse create(@Valid @RequestBody ReviewRequest request) {
        log.info("requête reçue sur le port {}", port);   // pour voir la Gateway alterner entre deux instances
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un avis", description = "Seuls la note et le commentaire changent.")
    public ReviewResponse update(@PathVariable UUID id, @Valid @RequestBody ReviewUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un avis")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
