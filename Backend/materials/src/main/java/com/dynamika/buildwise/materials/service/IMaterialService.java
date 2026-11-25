package com.dynamika.buildwise.materials.service;

import com.dynamika.buildwise.materials.dto.MaterialRequest;
import com.dynamika.buildwise.materials.dto.MaterialResponse;
import java.util.List;
import java.util.UUID;

public interface IMaterialService {
    List<MaterialResponse> findAll();

    MaterialResponse findById(UUID id);

    MaterialResponse create(MaterialRequest request);

    MaterialResponse update(UUID id, MaterialRequest request);

    void delete(UUID id);
}
