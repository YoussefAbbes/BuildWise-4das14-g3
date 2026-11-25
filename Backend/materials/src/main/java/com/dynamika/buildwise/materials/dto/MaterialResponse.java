package com.dynamika.buildwise.materials.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record MaterialResponse(UUID id, String name, String unit, BigDecimal unitPrice, String category,
                               Map<String, Object> specs) {}
