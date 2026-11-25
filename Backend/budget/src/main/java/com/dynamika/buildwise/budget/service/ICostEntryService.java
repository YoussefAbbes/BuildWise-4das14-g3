package com.dynamika.buildwise.budget.service;

import com.dynamika.buildwise.budget.dto.CostEntryRequest;
import com.dynamika.buildwise.budget.dto.CostEntryResponse;
import java.util.List;
import java.util.UUID;

public interface ICostEntryService {
    /** Les coûts d'une ligne ; 404 si la ligne n'existe pas. */
    List<CostEntryResponse> findAll(UUID lineId);

    /** 404 si le coût n'existe pas ou appartient à une autre ligne. */
    CostEntryResponse findById(UUID lineId, UUID id);

    CostEntryResponse create(UUID lineId, CostEntryRequest request);

    CostEntryResponse update(UUID lineId, UUID id, CostEntryRequest request);

    void delete(UUID lineId, UUID id);

    /** Tous les coûts d'un chantier, toutes lignes confondues. */
    List<CostEntryResponse> findByProject(UUID projectId);
}
