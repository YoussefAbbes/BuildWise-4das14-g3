package com.dynamika.buildwise.evaluation.model;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

// workerId (workforce), taskId et projectId (planning), authorId (identity) : seulement les identifiants.
// L'index unique refuse un deuxième avis du même ouvrier pour la même tâche.
@Document("reviews")
@CompoundIndex(name = "worker_task", def = "{'workerId': 1, 'taskId': 1}", unique = true)
public record Review(@Id UUID id, UUID workerId, UUID taskId, UUID projectId, int score, String comment,
                     UUID authorId, Instant createdAt) {}
