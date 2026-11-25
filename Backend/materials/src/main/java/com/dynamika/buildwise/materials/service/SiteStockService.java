package com.dynamika.buildwise.materials.service;

import com.dynamika.buildwise.materials.dto.SiteStockRequest;
import com.dynamika.buildwise.materials.dto.SiteStockResponse;
import com.dynamika.buildwise.materials.exception.ConflictException;
import com.dynamika.buildwise.materials.exception.NotFoundException;
import com.dynamika.buildwise.materials.mapper.MaterialsMapper;
import com.dynamika.buildwise.materials.model.SiteStock;
import com.dynamika.buildwise.materials.repository.MaterialRepository;
import com.dynamika.buildwise.materials.repository.ShortageReportRepository;
import com.dynamika.buildwise.materials.repository.SiteStockRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class SiteStockService implements ISiteStockService {
    private final SiteStockRepository stocks;
    private final MaterialRepository materials;
    private final ShortageReportRepository shortages;
    private final MaterialsMapper mapper;

    public SiteStockService(SiteStockRepository stocks, MaterialRepository materials,
                            ShortageReportRepository shortages, MaterialsMapper mapper) {
        this.stocks = stocks;
        this.materials = materials;
        this.shortages = shortages;
        this.mapper = mapper;
    }

    @Override
    public List<SiteStockResponse> findAll(UUID materialId) {
        checkMaterial(materialId);
        return stocks.findByMaterialId(materialId).stream().map(mapper::toResponse).toList();
    }

    // Une ligne d'un autre matériau est traitée comme absente : le chemin doit être cohérent.
    @Override
    public SiteStockResponse findById(UUID materialId, UUID id) {
        checkMaterial(materialId);
        return stocks.findById(id).filter(s -> s.materialId().equals(materialId)).map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("stock de chantier introuvable : " + id));
    }

    // Le mapper crée l'id ; on le lit avant d'enregistrer pour vérifier l'unicité.
    @Override
    public SiteStockResponse create(UUID materialId, SiteStockRequest request) {
        checkMaterial(materialId);
        SiteStock stock = mapper.toModel(materialId, request);
        checkUnique(stock.id(), request.projectId(), materialId);
        return mapper.toResponse(stocks.save(stock));
    }

    @Override
    public SiteStockResponse update(UUID materialId, UUID id, SiteStockRequest request) {
        findById(materialId, id);
        checkUnique(id, request.projectId(), materialId);
        return mapper.toResponse(stocks.save(mapper.toModel(id, materialId, request)));
    }

    // MongoDB n'a pas de clé étrangère : c'est le service qui protège la relation.
    @Override
    public void delete(UUID materialId, UUID id) {
        findById(materialId, id);
        if (shortages.existsBySiteStockId(id)) {
            throw new ConflictException("ce stock a encore des signalements de pénurie");
        }
        stocks.deleteById(id);
    }

    @Override
    public List<SiteStockResponse> findByProject(UUID projectId) {
        return stocks.findByProjectId(projectId).stream().map(mapper::toResponse).toList();
    }

    private void checkMaterial(UUID materialId) {
        if (!materials.existsById(materialId)) {
            throw new NotFoundException("matériau introuvable : " + materialId);
        }
    }

    // Une seule ligne de stock par couple (chantier, matériau), hormis la ligne elle-même à la modification.
    private void checkUnique(UUID id, UUID projectId, UUID materialId) {
        stocks.findByProjectIdAndMaterialId(projectId, materialId).map(SiteStock::id)
                .filter(other -> !other.equals(id))
                .ifPresent(other -> {
                    throw new ConflictException("ce chantier a déjà une ligne de stock pour ce matériau : " + other);
                });
    }
}
