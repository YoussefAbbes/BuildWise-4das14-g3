package com.dynamika.buildwise.materials.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

// reportedAt n'est pas dans le corps : le service met l'heure de création.
@Schema(description = "Signaler une pénurie sur une ligne de stock")
public record ShortageReportRequest(
        @Schema(example = "f0000000-0000-0000-0000-000000000001",
                description = "id rendu par POST /materials/{materialId}/site-stocks")
        @NotNull UUID siteStockId,
        @Schema(example = "30") @NotNull @Positive BigDecimal missingQty) {}
