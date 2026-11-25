package com.dynamika.buildwise.materials.model;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// specs change d'une catégorie à l'autre (ciment, acier, bois) : c'est la raison du choix de MongoDB.
@Document("materials")
public record Material(@Id UUID id, String name, String unit, BigDecimal unitPrice, String category,
                       Map<String, Object> specs) {}
