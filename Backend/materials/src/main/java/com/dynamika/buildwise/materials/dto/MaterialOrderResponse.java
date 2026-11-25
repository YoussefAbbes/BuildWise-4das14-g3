package com.dynamika.buildwise.materials.dto;

import com.dynamika.buildwise.materials.enums.OrderStatus;
import java.math.BigDecimal;
import java.util.UUID;

public record MaterialOrderResponse(UUID id, UUID projectId, UUID materialId, BigDecimal quantity, OrderStatus status) {}
