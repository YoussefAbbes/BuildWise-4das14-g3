package com.dynamika.buildwise.materials.repository;

import com.dynamika.buildwise.materials.model.ShortageReport;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShortageReportRepository extends MongoRepository<ShortageReport, UUID> {
    List<ShortageReport> findBySiteStockId(UUID siteStockId);

    boolean existsBySiteStockId(UUID siteStockId);
}
