package com.dynamika.buildwise.search.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

// Copie d'un ouvrier (workforce) avec sa note moyenne (evaluation), index "workers".
@Schema(description = "Document de l'index workers")
public record WorkerDoc(
        @Schema(example = "a0000000-0000-0000-0000-000000000001") @NotNull UUID id,
        @Schema(example = "Ali Ben Salah") @NotBlank String fullName,
        @Schema(example = "[\"MASON\"]") List<String> skills,
        @Schema(example = "4.50") BigDecimal score) {}
