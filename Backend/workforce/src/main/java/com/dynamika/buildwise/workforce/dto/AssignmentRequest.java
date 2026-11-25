package com.dynamika.buildwise.workforce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Affecter un ouvrier à une tâche sur une période")
public record AssignmentRequest(
        @Schema(example = "a0000000-0000-0000-0000-000000000001") @NotNull UUID workerId,
        @Schema(example = "e0000000-0000-0000-0000-000000000001") @NotNull UUID taskId,
        @Schema(example = "2026-12-01") @NotNull LocalDate from,
        @Schema(example = "2026-12-05") @NotNull LocalDate to) {}
