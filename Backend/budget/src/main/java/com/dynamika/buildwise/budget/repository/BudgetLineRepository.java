package com.dynamika.buildwise.budget.repository;

import com.dynamika.buildwise.budget.model.BudgetLine;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BudgetLineRepository extends JpaRepository<BudgetLine, UUID> {
    List<BudgetLine> findByProjectId(UUID projectId);

    // La somme est faite par la base ; 0 si le chantier n'a aucune ligne.
    @Query("select coalesce(sum(b.amount), 0) from BudgetLine b where b.projectId = ?1")
    BigDecimal total(UUID projectId);
}
