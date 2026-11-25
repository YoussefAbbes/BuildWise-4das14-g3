package com.dynamika.buildwise.materials.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// ShortageReport * -- 1 SiteStock : la relation est le champ siteStockId.
@Document("shortage_reports")
public record ShortageReport(@Id UUID id, UUID siteStockId, BigDecimal missingQty, Instant reportedAt) {}

