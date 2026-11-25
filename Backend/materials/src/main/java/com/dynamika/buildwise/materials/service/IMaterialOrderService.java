package com.dynamika.buildwise.materials.service;

import com.dynamika.buildwise.materials.dto.MaterialOrderRequest;
import com.dynamika.buildwise.materials.dto.MaterialOrderResponse;
import com.dynamika.buildwise.materials.dto.MaterialOrderUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface IMaterialOrderService {
    /** projectId null : toutes les commandes. */
    List<MaterialOrderResponse> findAll(UUID projectId);

    MaterialOrderResponse findById(UUID id);

    MaterialOrderResponse create(MaterialOrderRequest request);

    MaterialOrderResponse update(UUID id, MaterialOrderUpdateRequest request);

    void delete(UUID id);
}
