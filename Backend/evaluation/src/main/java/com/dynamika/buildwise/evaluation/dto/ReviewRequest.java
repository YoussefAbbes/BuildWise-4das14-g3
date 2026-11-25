package com.dynamika.buildwise.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Noter le travail d'un ouvrier sur une tâche")
public record ReviewRequest(
        @Schema(example = "a0000000-0000-0000-0000-000000000001") @NotNull UUID workerId,
        @Schema(example = "e0000000-0000-0000-0000-000000000001") @NotNull UUID taskId,
        @Schema(example = "d0000000-0000-0000-0000-000000000001") @NotNull UUID projectId,
        @Schema(example = "4") @Min(1) @Max(5) int score,
        @Schema(example = "Travail propre et dans les délais") String comment,
        @Schema(example = "f0000000-0000-0000-0000-000000000001") @NotNull UUID authorId) {}
