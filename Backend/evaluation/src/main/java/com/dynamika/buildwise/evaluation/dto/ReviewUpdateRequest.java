package com.dynamika.buildwise.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "Modifier la note et le commentaire d'un avis")
public record ReviewUpdateRequest(
        @Schema(example = "5") @Min(1) @Max(5) int score,
        @Schema(example = "Très bon travail") String comment) {}
