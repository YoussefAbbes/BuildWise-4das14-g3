package com.dynamika.buildwise.evaluation.dto;

import java.time.Instant;
import java.util.UUID;

public record ReviewResponse(UUID id, UUID workerId, UUID taskId, UUID projectId, int score, String comment,
                             UUID authorId, Instant createdAt) {}
