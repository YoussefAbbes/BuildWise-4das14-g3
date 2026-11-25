package com.dynamika.buildwise.search.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

// Copie en lecture seule d'un projet (index "projects") : corps de la requête et document stocké.
@Schema(description = "Document de l'index projects")
public record ProjectDoc(
        @Schema(example = "d0000000-0000-0000-0000-000000000001") @NotNull UUID id,
        @Schema(example = "Résidence Les Jasmins") @NotBlank String name,
        @Schema(example = "IN_PROGRESS") @NotBlank String status) {}
