package com.dynamika.buildwise.materials.repository;

import com.dynamika.buildwise.materials.model.MaterialOrder;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MaterialOrderRepository extends MongoRepository<MaterialOrder, UUID> {
    List<MaterialOrder> findByProjectId(UUID projectId);

    boolean existsByMaterialId(UUID materialId);
}

