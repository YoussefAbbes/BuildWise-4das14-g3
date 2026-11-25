package com.dynamika.buildwise.evaluation.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WorkerScoreResponse(UUID workerId, String fullName, BigDecimal average, int count) {}
