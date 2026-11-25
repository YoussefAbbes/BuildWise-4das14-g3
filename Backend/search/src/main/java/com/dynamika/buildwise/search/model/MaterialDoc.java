package com.dynamika.buildwise.search.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

// Copie d'un matériau (materials), index "materials".
@Schema(description = "Document de l'index materials")
public record MaterialDoc(
        @Schema(example = "b0000000-0000-0000-0000-000000000001") @NotNull UUID id,
        @Schema(example = "Ciment CEM II 42,5") @NotBlank String name) {}
