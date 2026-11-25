package com.dynamika.buildwise.materials.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

// Le materialId vient du chemin : /materials/{materialId}/site-stocks.
@Schema(description = "Stock d'un matériau sur un chantier, et le seuil d'alerte")
public record SiteStockRequest(
        @Schema(example = "d0000000-0000-0000-0000-000000000001") @NotNull UUID projectId,
        @Schema(example = "120") @NotNull @PositiveOrZero BigDecimal quantity,
        @Schema(example = "20") @NotNull @PositiveOrZero BigDecimal threshold) {}
