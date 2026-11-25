package com.dynamika.buildwise.materials.model;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// SiteStock * -- 1 Material : la relation est le champ materialId. projectId vient du service projects.
@Document("site_stocks")
public record SiteStock(@Id UUID id, UUID projectId, UUID materialId, BigDecimal quantity, BigDecimal threshold) {}

