package com.dynamika.buildwise.budget.dto;

import com.dynamika.buildwise.budget.enums.CostSource;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Un coût réel imputé à une ligne de budget")
public record CostEntryRequest(
        @Schema(example = "MATERIAL") @NotNull CostSource source,
        @Schema(example = "1250.00") @NotNull @Positive @Digits(integer = 12, fraction = 2) BigDecimal amount,
        @Schema(example = "f0000000-0000-0000-0000-000000000001",
                description = "Facultatif. Id de l'événement source ; 409 s'il est déjà utilisé.") UUID eventId,
        @Schema(example = "2026-12-01T08:00:00Z", description = "Facultatif : maintenant si absent.") Instant occurredAt) {}
