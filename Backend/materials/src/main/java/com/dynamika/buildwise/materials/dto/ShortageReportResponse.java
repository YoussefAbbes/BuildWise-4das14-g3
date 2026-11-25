package com.dynamika.buildwise.materials.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ShortageReportResponse(UUID id, UUID siteStockId, BigDecimal missingQty, Instant reportedAt) {}
