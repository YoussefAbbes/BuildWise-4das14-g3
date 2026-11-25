package com.dynamika.buildwise.workforce.model;

import com.dynamika.buildwise.workforce.enums.Skill;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("workers")
public record Worker(@Id UUID id, UUID companyId, String fullName, BigDecimal hourlyRate, Set<Skill> skills) {}
