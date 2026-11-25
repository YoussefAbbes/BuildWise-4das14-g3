package com.dynamika.buildwise.materials.service;

import com.dynamika.buildwise.materials.dto.MaterialOrderRequest;
import com.dynamika.buildwise.materials.dto.MaterialOrderResponse;
import com.dynamika.buildwise.materials.dto.MaterialOrderUpdateRequest;
import com.dynamika.buildwise.materials.exception.NotFoundException;
import com.dynamika.buildwise.materials.mapper.MaterialsMapper;
import com.dynamika.buildwise.materials.model.MaterialOrder;
import com.dynamika.buildwise.materials.repository.MaterialOrderRepository;
import com.dynamika.buildwise.materials.repository.MaterialRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MaterialOrderService implements IMaterialOrderService {
    private final MaterialOrderRepository orders;
    private final MaterialRepository materials;
    private final MaterialsMapper mapper;

    public MaterialOrderService(MaterialOrderRepository orders, MaterialRepository materials, MaterialsMapper mapper) {
        this.orders = orders;
        this.materials = materials;
        this.mapper = mapper;
    }

    @Override
    public List<MaterialOrderResponse> findAll(UUID projectId) {
        List<MaterialOrder> found = projectId == null ? orders.findAll() : orders.findByProjectId(projectId);
        return found.stream().map(mapper::toResponse).toList();
    }

    @Override
    public MaterialOrderResponse findById(UUID id) {
        return orders.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("commande introuvable : " + id));
    }

    // Le mapper crée l'id, et une nouvelle commande est toujours ORDERED (voir le mapper).
    @Override
    public MaterialOrderResponse create(MaterialOrderRequest request) {
        checkMaterial(request.materialId());
        return mapper.toResponse(orders.save(mapper.toModel(request)));
    }

    // Plus tard : passer à DELIVERED ajoutera la quantité au stock du chantier (pas fait ici).
    @Override
    public MaterialOrderResponse update(UUID id, MaterialOrderUpdateRequest request) {
        findById(id);
        checkMaterial(request.materialId());
        return mapper.toResponse(orders.save(mapper.toModel(id, request)));
    }

    @Override
    public void delete(UUID id) {
        findById(id);
        orders.deleteById(id);
    }

    private void checkMaterial(UUID materialId) {
        if (!materials.existsById(materialId)) {
            throw new NotFoundException("matériau introuvable : " + materialId);
        }
    }
}
