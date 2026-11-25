package com.dynamika.buildwise.evaluation.repository;

import com.dynamika.buildwise.evaluation.model.Review;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, UUID> {
    List<Review> findByWorkerId(UUID workerId);

    boolean existsByWorkerIdAndTaskId(UUID workerId, UUID taskId);
}
