package com.dynamika.buildwise.materials.model;

import com.dynamika.buildwise.materials.enums.OrderStatus;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Material 1 -- * MaterialOrder : la relation est le champ materialId. projectId vient du service projects.
@Document("material_orders")
public record MaterialOrder(@Id UUID id, UUID projectId, UUID materialId, BigDecimal quantity, OrderStatus status) {}

