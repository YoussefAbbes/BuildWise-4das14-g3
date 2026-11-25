package com.dynamika.buildwise.budget.dto;

import com.dynamika.buildwise.budget.enums.CostSource;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CostEntryResponse(UUID id, UUID budgetLineId, CostSource source, BigDecimal amount, UUID eventId,
                                Instant occurredAt) {}
