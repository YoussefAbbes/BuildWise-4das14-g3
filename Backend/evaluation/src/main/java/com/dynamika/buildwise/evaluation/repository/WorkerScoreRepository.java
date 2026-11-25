package com.dynamika.buildwise.evaluation.repository;

import com.dynamika.buildwise.evaluation.model.WorkerScore;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkerScoreRepository extends MongoRepository<WorkerScore, UUID> {}
