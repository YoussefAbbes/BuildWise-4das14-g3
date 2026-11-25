package com.dynamika.buildwise.budget.service;

import com.dynamika.buildwise.budget.dto.BudgetLineRequest;
import com.dynamika.buildwise.budget.dto.BudgetLineResponse;
import com.dynamika.buildwise.budget.dto.ProjectBudget;
import com.dynamika.buildwise.budget.exception.NotFoundException;
import com.dynamika.buildwise.budget.mapper.BudgetMapper;
import com.dynamika.buildwise.budget.model.BudgetLine;
import com.dynamika.buildwise.budget.repository.BudgetLineRepository;
import com.dynamika.buildwise.budget.repository.CostEntryRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BudgetLineService implements IBudgetLineService {
    private final BudgetLineRepository lines;
    private final CostEntryRepository costs;
    private final BudgetMapper mapper;

    public BudgetLineService(BudgetLineRepository lines, CostEntryRepository costs, BudgetMapper mapper) {
        this.lines = lines;
        this.costs = costs;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetLineResponse> findAll(UUID projectId) {
        List<BudgetLine> found = projectId == null ? lines.findAll() : lines.findByProjectId(projectId);
        return found.stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetLineResponse findById(UUID id) {
        return mapper.toResponse(find(id));
    }

    // Hibernate génère l'UUID au save.
    @Override
    public BudgetLineResponse create(BudgetLineRequest request) {
        return mapper.toResponse(lines.save(mapper.toModel(request)));
    }

    // L'entité chargée est suivie par Hibernate : les changements sont écrits au commit, sans save.
    @Override
    public BudgetLineResponse update(UUID id, BudgetLineRequest request) {
        return mapper.toResponse(mapper.update(find(id), request));
    }

    // cascade = ALL + orphanRemoval : les coûts de la ligne partent avec elle.
    @Override
    public void delete(UUID id) {
        lines.delete(find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectBudget budget(UUID projectId) {
        BigDecimal total = lines.total(projectId);
        BigDecimal spent = costs.spent(projectId);
        return new ProjectBudget(projectId, total, spent, total.subtract(spent));
    }

    private BudgetLine find(UUID id) {
        return lines.findById(id).orElseThrow(() -> new NotFoundException("ligne de budget introuvable : " + id));
    }
}
