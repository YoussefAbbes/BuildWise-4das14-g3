package com.dynamika.buildwise.workforce.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TimesheetResponse(UUID id, UUID workerId, UUID taskId, LocalDate date, BigDecimal hours) {}
