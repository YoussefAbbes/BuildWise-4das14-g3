package com.dynamika.buildwise.budget.dto;

import com.dynamika.buildwise.budget.enums.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Données pour créer ou modifier une ligne de budget")
public record BudgetLineRequest(
        @Schema(example = "d0000000-0000-0000-0000-000000000001") @NotNull UUID projectId,
        @Schema(example = "MATERIALS") @NotNull Category category,
        // NUMERIC(14,2) : au plus 12 chiffres avant la virgule et 2 après
        @Schema(example = "15000.00") @NotNull @Positive @Digits(integer = 12, fraction = 2) BigDecimal amount) {}
