package com.dynamika.buildwise.materials.repository;

import com.dynamika.buildwise.materials.model.SiteStock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SiteStockRepository extends MongoRepository<SiteStock, UUID> {
    List<SiteStock> findByMaterialId(UUID materialId);

    List<SiteStock> findByProjectId(UUID projectId);

    Optional<SiteStock> findByProjectIdAndMaterialId(UUID projectId, UUID materialId);

    boolean existsByMaterialId(UUID materialId);
}
