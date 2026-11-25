package com.dynamika.buildwise.materials.mapper;

import com.dynamika.buildwise.materials.dto.MaterialOrderRequest;
import com.dynamika.buildwise.materials.dto.MaterialOrderResponse;
import com.dynamika.buildwise.materials.dto.MaterialOrderUpdateRequest;
import com.dynamika.buildwise.materials.dto.MaterialRequest;
import com.dynamika.buildwise.materials.dto.MaterialResponse;
import com.dynamika.buildwise.materials.dto.ShortageReportRequest;
import com.dynamika.buildwise.materials.dto.ShortageReportResponse;
import com.dynamika.buildwise.materials.dto.SiteStockRequest;
import com.dynamika.buildwise.materials.dto.SiteStockResponse;
import com.dynamika.buildwise.materials.model.Material;
import com.dynamika.buildwise.materials.model.MaterialOrder;
import com.dynamika.buildwise.materials.model.ShortageReport;
import com.dynamika.buildwise.materials.model.SiteStock;
import java.time.Instant;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Convertit entre les documents (model) et les DTO.
 * MapStruct écrit le code à la compilation (MaterialsMapperImpl) : on déclare seulement les méthodes.
 * Spring l'injecte comme un bean. Classe abstraite : elle peut aussi contenir nos propres méthodes (newId).
 *
 * Deux toModel par classe : sans id pour créer (le mapper en génère un), avec id pour modifier (celui du chemin).
 * Les paramètres en plus (materialId, reportedAt) sont copiés dans le champ du même nom.
 */
@Mapper(componentModel = "spring")
public abstract class MaterialsMapper {

    // MongoDB ne génère pas les UUID : c'est le mapper qui le crée.
    protected UUID newId() {
        return UUID.randomUUID();
    }

    @Mapping(target = "id", expression = "java(newId())")
    @Mapping(target = "specs", defaultExpression = "java(java.util.Map.of())")
    public abstract Material toModel(MaterialRequest request);

    @Mapping(target = "specs", defaultExpression = "java(java.util.Map.of())")
    public abstract Material toModel(UUID id, MaterialRequest request);

    public abstract MaterialResponse toResponse(Material material);

    // materialId vient du chemin /materials/{materialId}/site-stocks.
    @Mapping(target = "id", expression = "java(newId())")
    public abstract SiteStock toModel(UUID materialId, SiteStockRequest request);

    public abstract SiteStock toModel(UUID id, UUID materialId, SiteStockRequest request);

    public abstract SiteStockResponse toResponse(SiteStock siteStock);

    // reportedAt : maintenant à la création, l'ancienne valeur à la modification.
    @Mapping(target = "id", expression = "java(newId())")
    @Mapping(target = "reportedAt", expression = "java(java.time.Instant.now())")
    public abstract ShortageReport toModel(ShortageReportRequest request);

    public abstract ShortageReport toModel(UUID id, ShortageReportRequest request, Instant reportedAt);

    public abstract ShortageReportResponse toResponse(ShortageReport shortageReport);

    // Une nouvelle commande est toujours ORDERED.
    @Mapping(target = "id", expression = "java(newId())")
    @Mapping(target = "status", constant = "ORDERED")
    public abstract MaterialOrder toModel(MaterialOrderRequest request);

    public abstract MaterialOrder toModel(UUID id, MaterialOrderUpdateRequest request);

    public abstract MaterialOrderResponse toResponse(MaterialOrder order);
}
