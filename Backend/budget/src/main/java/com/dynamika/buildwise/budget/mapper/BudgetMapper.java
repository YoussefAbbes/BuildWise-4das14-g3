package com.dynamika.buildwise.budget.mapper;

import com.dynamika.buildwise.budget.dto.BudgetLineRequest;
import com.dynamika.buildwise.budget.dto.BudgetLineResponse;
import com.dynamika.buildwise.budget.dto.CostEntryRequest;
import com.dynamika.buildwise.budget.dto.CostEntryResponse;
import com.dynamika.buildwise.budget.model.BudgetLine;
import com.dynamika.buildwise.budget.model.CostEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Convertit entre les entités (model) et les DTO.
 * MapStruct écrit le code à la compilation (BudgetMapperImpl) : on déclare seulement les méthodes.
 * Spring l'injecte comme un bean.
 *
 * Pas d'id ici : Hibernate le génère à l'enregistrement. Les relations (costEntries, budgetLine) sont gérées
 * par le service. À la modification, le service passe l'entité chargée à update(...) et Hibernate enregistre
 * les changements à la fin de la transaction.
 */
@Mapper(componentModel = "spring")
public abstract class BudgetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "costEntries", ignore = true)
    public abstract BudgetLine toModel(BudgetLineRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "costEntries", ignore = true)
    public abstract BudgetLine update(@MappingTarget BudgetLine line, BudgetLineRequest request);

    public abstract BudgetLineResponse toResponse(BudgetLine line);

    // occurredAt absent à la création : maintenant.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "budgetLine", ignore = true)
    @Mapping(target = "occurredAt", defaultExpression = "java(java.time.Instant.now())")
    public abstract CostEntry toModel(CostEntryRequest request);

    // occurredAt absent à la modification : on garde la date déjà enregistrée (eventId absent devient null).
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "budgetLine", ignore = true)
    @Mapping(target = "occurredAt", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract CostEntry update(@MappingTarget CostEntry entry, CostEntryRequest request);

    // À appeler dans la transaction : budgetLine est chargée à la demande (LAZY).
    @Mapping(source = "budgetLine.id", target = "budgetLineId")
    public abstract CostEntryResponse toResponse(CostEntry entry);
}
