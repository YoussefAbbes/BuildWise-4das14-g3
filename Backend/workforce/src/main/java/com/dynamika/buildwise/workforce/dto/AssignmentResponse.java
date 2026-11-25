package com.dynamika.buildwise.workforce.dto;

import java.time.LocalDate;
import java.util.UUID;

public record AssignmentResponse(UUID id, UUID workerId, UUID taskId, LocalDate from, LocalDate to) {}
