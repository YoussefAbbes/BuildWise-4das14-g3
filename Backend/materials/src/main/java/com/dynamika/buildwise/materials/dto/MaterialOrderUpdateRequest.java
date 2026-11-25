package com.dynamika.buildwise.materials.dto;

import com.dynamika.buildwise.materials.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

// Corps du PUT : les champs de la création, plus le statut.
@Schema(description = "Modifier une commande, statut compris")
public record MaterialOrderUpdateRequest(
        @Schema(example = "d0000000-0000-0000-0000-000000000001") @NotNull UUID projectId,
        @Schema(example = "b0000000-0000-0000-0000-000000000001") @NotNull UUID materialId,
        @Schema(example = "50") @NotNull @Positive BigDecimal quantity,
        @Schema(example = "CANCELLED") @NotNull OrderStatus status) {}
