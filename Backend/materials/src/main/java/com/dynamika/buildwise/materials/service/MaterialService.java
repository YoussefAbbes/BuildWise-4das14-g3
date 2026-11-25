package com.dynamika.buildwise.materials.service;

import com.dynamika.buildwise.materials.dto.MaterialRequest;
import com.dynamika.buildwise.materials.dto.MaterialResponse;
import com.dynamika.buildwise.materials.exception.ConflictException;
import com.dynamika.buildwise.materials.exception.NotFoundException;
import com.dynamika.buildwise.materials.mapper.MaterialsMapper;
import com.dynamika.buildwise.materials.repository.MaterialOrderRepository;
import com.dynamika.buildwise.materials.repository.MaterialRepository;
import com.dynamika.buildwise.materials.repository.SiteStockRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MaterialService implements IMaterialService {
    private final MaterialRepository materials;
    private final SiteStockRepository stocks;
    private final MaterialOrderRepository orders;
    private final MaterialsMapper mapper;

    public MaterialService(MaterialRepository materials, SiteStockRepository stocks, MaterialOrderRepository orders,
                           MaterialsMapper mapper) {
        this.materials = materials;
        this.stocks = stocks;
        this.orders = orders;
        this.mapper = mapper;
    }

    // Cache Redis plus tard : @Cacheable ici, @CacheEvict(allEntries = true) sur create, update et delete.
    @Override
    public List<MaterialResponse> findAll() {
        return materials.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public MaterialResponse findById(UUID id) {
        return materials.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("matériau introuvable : " + id));
    }

    // MongoDB ne génère pas les UUID : le mapper en crée un (toModel sans id).
    @Override
    public MaterialResponse create(MaterialRequest request) {
        return mapper.toResponse(materials.save(mapper.toModel(request)));
    }

    @Override
    public MaterialResponse update(UUID id, MaterialRequest request) {
        findById(id);
        return mapper.toResponse(materials.save(mapper.toModel(id, request)));
    }

    // MongoDB n'a pas de clé étrangère : c'est le service qui protège la relation.
    @Override
    public void delete(UUID id) {
        findById(id);
        if (stocks.existsByMaterialId(id) || orders.existsByMaterialId(id)) {
            throw new ConflictException("ce matériau a encore des stocks ou des commandes");
        }
        materials.deleteById(id);
    }
}
