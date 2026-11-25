package com.dynamika.buildwise.budget.service;

import com.dynamika.buildwise.budget.dto.BudgetLineRequest;
import com.dynamika.buildwise.budget.dto.BudgetLineResponse;
import com.dynamika.buildwise.budget.dto.ProjectBudget;
import java.util.List;
import java.util.UUID;

public interface IBudgetLineService {
    /** projectId null : toutes les lignes. */
    List<BudgetLineResponse> findAll(UUID projectId);

    BudgetLineResponse findById(UUID id);

    BudgetLineResponse create(BudgetLineRequest request);

    BudgetLineResponse update(UUID id, BudgetLineRequest request);

    /** Supprime aussi les coûts de la ligne (cascade). */
    void delete(UUID id);

    /** Budget prévu, dépensé et restant d'un chantier ; 0 partout s'il n'a rien. */
    ProjectBudget budget(UUID projectId);
}
