package com.dynamika.buildwise.workforce.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("timesheets")
public record Timesheet(@Id UUID id, UUID workerId, UUID taskId, LocalDate date, BigDecimal hours) {}
