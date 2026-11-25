package com.dynamika.buildwise.materials.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SiteStockResponse(UUID id, UUID projectId, UUID materialId, BigDecimal quantity, BigDecimal threshold) {}
