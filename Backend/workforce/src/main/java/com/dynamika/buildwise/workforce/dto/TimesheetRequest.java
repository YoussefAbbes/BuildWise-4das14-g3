package com.dynamika.buildwise.workforce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Pointer les heures d'un ouvrier sur une tâche")
public record TimesheetRequest(
        @Schema(example = "a0000000-0000-0000-0000-000000000001") @NotNull UUID workerId,
        @Schema(example = "e0000000-0000-0000-0000-000000000001") @NotNull UUID taskId,
        @Schema(example = "2026-12-01") @NotNull LocalDate date,
        @Schema(example = "7.5") @NotNull @Positive @DecimalMax("24") BigDecimal hours) {}
