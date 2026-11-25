package com.dynamika.buildwise.search.model;

import com.dynamika.buildwise.search.enums.AlertKind;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

// Une alerte (retard, dépassement de budget, rupture de stock), index "alerts".
@Schema(description = "Document de l'index alerts")
public record AlertDoc(
        @Schema(example = "f0000000-0000-0000-0000-000000000001") @NotNull UUID id,
        @Schema(example = "Résidence Les Jasmins") @NotBlank String project,
        @Schema(example = "MATERIAL_SHORTAGE") @NotNull AlertKind kind,
        @Schema(example = "stock de ciment sous le seuil") @NotBlank String detail) {}
