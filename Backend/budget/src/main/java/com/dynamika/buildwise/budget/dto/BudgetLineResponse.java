package com.dynamika.buildwise.budget.dto;

import com.dynamika.buildwise.budget.enums.Category;
import java.math.BigDecimal;
import java.util.UUID;

public record BudgetLineResponse(UUID id, UUID projectId, Category category, BigDecimal amount) {}
