package com.dynamika.buildwise.workforce.dto;

import com.dynamika.buildwise.workforce.enums.Skill;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Données pour créer ou modifier un ouvrier")
public record WorkerRequest(
        @Schema(example = "c0000000-0000-0000-0000-000000000001") @NotNull UUID companyId,
        @Schema(example = "Ali Ben Salah") @NotBlank String fullName,
        @Schema(example = "18.50") @NotNull @Positive BigDecimal hourlyRate,
        @Schema(example = "[\"MASON\"]") Set<Skill> skills) {}
