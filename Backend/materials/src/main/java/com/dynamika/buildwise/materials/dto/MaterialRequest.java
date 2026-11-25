package com.dynamika.buildwise.materials.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Map;

@Schema(description = "Données pour créer ou modifier un matériau du catalogue")
public record MaterialRequest(
        @Schema(example = "Ciment CEM II 42,5") @NotBlank String name,
        @Schema(example = "sac") @NotBlank String unit,
        @Schema(example = "12.90") @NotNull @Positive BigDecimal unitPrice,
        @Schema(example = "ciment") @NotBlank String category,
        @Schema(example = "{\"classe\":\"42,5 N\",\"poidsSacKg\":50}") Map<String, Object> specs) {}

