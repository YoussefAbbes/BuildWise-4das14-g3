package com.dynamika.buildwise.workforce.dto;

import com.dynamika.buildwise.workforce.enums.Skill;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record WorkerResponse(UUID id, UUID companyId, String fullName, BigDecimal hourlyRate, Set<Skill> skills) {}
