package com.dynamika.buildwise.materials.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

// Pas de statut à la création : une commande commence toujours à ORDERED.
@Schema(description = "Commander un matériau pour un chantier")
public record MaterialOrderRequest(
        @Schema(example = "d0000000-0000-0000-0000-000000000001") @NotNull UUID projectId,
        @Schema(example = "b0000000-0000-0000-0000-000000000001") @NotNull UUID materialId,
        @Schema(example = "50") @NotNull @Positive BigDecimal quantity) {}
