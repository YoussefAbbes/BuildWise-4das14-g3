package com.dynamika.buildwise.search.controller;

import com.dynamika.buildwise.search.enums.SearchType;
import com.dynamika.buildwise.search.model.AlertDoc;
import com.dynamika.buildwise.search.model.MaterialDoc;
import com.dynamika.buildwise.search.model.ProjectDoc;
import com.dynamika.buildwise.search.model.WorkerDoc;
import com.dynamika.buildwise.search.service.ISearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/search")
@Tag(name = "Search", description = "Recherche plein texte et remplissage des index Meilisearch")
public class SearchController {
    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    private final ISearchService service;
    private final int port;

    public SearchController(ISearchService service, @Value("${server.port}") int port) {
        this.service = service;
        this.port = port;
    }

    // q absent : 400 par Spring ; q vide : 400 ici.
    @GetMapping
    @Operation(summary = "Rechercher dans un index ou dans les quatre",
            description = "Sans type, les quatre index sont interrogés. Réponse : nom de l'index -> documents.")
    public Map<String, List<Map<String, Object>>> search(@RequestParam String q,
                                                         @RequestParam(required = false) @Parameter(schema = @Schema(allowableValues = {"projects", "workers", "materials", "alerts"})) String type) {
        log.info("requête reçue sur le port {}", port);   // pour voir la Gateway alterner entre deux instances
        if (q.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "le paramètre q est vide");
        }
        return service.search(q, type == null ? null : type(type));
    }

    @PostMapping("/projects")
    @Operation(summary = "Ajouter ou remplacer un projet dans l'index")
    public ProjectDoc saveProject(@Valid @RequestBody ProjectDoc doc) {
        return service.save(SearchType.PROJECTS, doc);
    }

    @PostMapping("/workers")
    @Operation(summary = "Ajouter ou remplacer un ouvrier dans l'index")
    public WorkerDoc saveWorker(@Valid @RequestBody WorkerDoc doc) {
        return service.save(SearchType.WORKERS, doc);
    }

    @PostMapping("/materials")
    @Operation(summary = "Ajouter ou remplacer un matériau dans l'index")
    public MaterialDoc saveMaterial(@Valid @RequestBody MaterialDoc doc) {
        return service.save(SearchType.MATERIALS, doc);
    }

    @PostMapping("/alerts")
    @Operation(summary = "Ajouter ou remplacer une alerte dans l'index")
    public AlertDoc saveAlert(@Valid @RequestBody AlertDoc doc) {
        return service.save(SearchType.ALERTS, doc);
    }

    @GetMapping("/{type}/{id}")
    @Operation(summary = "Lire un document", description = "404 si le document n'existe pas, 400 si le type est inconnu.")
    public Object find(@PathVariable @Parameter(schema = @Schema(allowableValues = {"projects", "workers", "materials", "alerts"})) String type,
                       @PathVariable UUID id) {
        return service.find(type(type), id);
    }

    @DeleteMapping("/{type}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un document", description = "204 même si le document n'existait pas.")
    public void delete(@PathVariable @Parameter(schema = @Schema(allowableValues = {"projects", "workers", "materials", "alerts"})) String type,
                       @PathVariable UUID id) {
        service.delete(type(type), id);
    }

    // Le type s'écrit comme le nom de l'index : "projects" -> PROJECTS. Inconnu : 400.
    private static SearchType type(String index) {
        return SearchType.fromIndex(index).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "type inconnu : " + index));
    }
}
