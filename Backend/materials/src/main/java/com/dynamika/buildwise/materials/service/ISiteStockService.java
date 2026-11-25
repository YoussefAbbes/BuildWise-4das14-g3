package com.dynamika.buildwise.materials.service;

import com.dynamika.buildwise.materials.dto.SiteStockRequest;
import com.dynamika.buildwise.materials.dto.SiteStockResponse;
import java.util.List;
import java.util.UUID;

public interface ISiteStockService {
    /** Les lignes de stock d'un matériau, sur tous les chantiers. */
    List<SiteStockResponse> findAll(UUID materialId);

    SiteStockResponse findById(UUID materialId, UUID id);

    SiteStockResponse create(UUID materialId, SiteStockRequest request);

    SiteStockResponse update(UUID materialId, UUID id, SiteStockRequest request);

    void delete(UUID materialId, UUID id);

    /** Les lignes de stock d'un chantier, tous matériaux confondus. */
    List<SiteStockResponse> findByProject(UUID projectId);
}
