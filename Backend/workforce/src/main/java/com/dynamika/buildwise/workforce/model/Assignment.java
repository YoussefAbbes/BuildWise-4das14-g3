package com.dynamika.buildwise.workforce.model;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("assignments")
public record Assignment(@Id UUID id, UUID workerId, UUID taskId, LocalDate from, LocalDate to) {}
