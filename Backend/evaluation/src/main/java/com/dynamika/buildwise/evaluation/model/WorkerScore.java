package com.dynamika.buildwise.evaluation.model;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// WorkerScore 1 -- * Review par workerId. Recalculé par le service après chaque avis créé, modifié ou supprimé.
@Document("worker_scores")
public record WorkerScore(@Id UUID workerId, String fullName, BigDecimal average, int count) {}
