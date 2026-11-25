package com.dynamika.buildwise.budget.repository;

import com.dynamika.buildwise.budget.model.CostEntry;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CostEntryRepository extends JpaRepository<CostEntry, UUID> {
    List<CostEntry> findByBudgetLineId(UUID budgetLineId);

    // Un coût n'est trouvé que dans sa propre ligne.
    Optional<CostEntry> findByIdAndBudgetLineId(UUID id, UUID budgetLineId);

    List<CostEntry> findByBudgetLineProjectId(UUID projectId);

    boolean existsByEventId(UUID eventId);

    // À la modification : l'eventId est-il pris par un autre coût ?
    boolean existsByEventIdAndIdNot(UUID eventId, UUID id);

    // Ce qui a déjà été dépensé sur le chantier ; 0 si aucun coût.
    @Query("select coalesce(sum(c.amount), 0) from CostEntry c where c.budgetLine.projectId = ?1")
    BigDecimal spent(UUID projectId);
}
