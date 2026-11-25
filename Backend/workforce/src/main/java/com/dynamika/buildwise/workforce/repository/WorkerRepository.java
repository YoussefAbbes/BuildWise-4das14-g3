package com.dynamika.buildwise.workforce.repository;

import com.dynamika.buildwise.workforce.model.Worker;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkerRepository extends MongoRepository<Worker, UUID> {}
