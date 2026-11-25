package com.dynamika.buildwise.budget.dto;

import java.math.BigDecimal;
import java.util.UUID;

// total = somme des lignes, spent = somme des coûts, remaining = total - spent (négatif en cas de dépassement).
public record ProjectBudget(UUID projectId, BigDecimal total, BigDecimal spent, BigDecimal remaining) {}
