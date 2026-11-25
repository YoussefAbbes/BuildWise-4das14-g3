package com.dynamika.buildwise.workforce.repository;

import com.dynamika.buildwise.workforce.model.Assignment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssignmentRepository extends MongoRepository<Assignment, UUID> {
    List<Assignment> findByWorkerId(UUID workerId);

    boolean existsByWorkerId(UUID workerId);
}
