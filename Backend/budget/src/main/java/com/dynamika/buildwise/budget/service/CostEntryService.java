package com.dynamika.buildwise.budget.service;

import com.dynamika.buildwise.budget.dto.CostEntryRequest;
import com.dynamika.buildwise.budget.dto.CostEntryResponse;
import com.dynamika.buildwise.budget.exception.ConflictException;
import com.dynamika.buildwise.budget.exception.NotFoundException;
import com.dynamika.buildwise.budget.mapper.BudgetMapper;
import com.dynamika.buildwise.budget.model.BudgetLine;
import com.dynamika.buildwise.budget.model.CostEntry;
import com.dynamika.buildwise.budget.repository.BudgetLineRepository;
import com.dynamika.buildwise.budget.repository.CostEntryRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CostEntryService implements ICostEntryService {
    private final CostEntryRepository costs;
    private final BudgetLineRepository lines;
    private final BudgetMapper mapper;

    public CostEntryService(CostEntryRepository costs, BudgetLineRepository lines, BudgetMapper mapper) {
        this.costs = costs;
        this.lines = lines;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CostEntryResponse> findAll(UUID lineId) {
        findLine(lineId);
        return costs.findByBudgetLineId(lineId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CostEntryResponse findById(UUID lineId, UUID id) {
        return mapper.toResponse(find(lineId, id));
    }

    @Override
    public CostEntryResponse create(UUID lineId, CostEntryRequest request) {
        BudgetLine line = findLine(lineId);
        if (request.eventId() != null && costs.existsByEventId(request.eventId())) {
            throw new ConflictException("eventId déjà utilisé : " + request.eventId());
        }
        CostEntry entry = mapper.toModel(request);
        line.addCostEntry(entry);
        return mapper.toResponse(costs.save(entry));
    }

    @Override
    public CostEntryResponse update(UUID lineId, UUID id, CostEntryRequest request) {
        CostEntry entry = find(lineId, id);
        if (request.eventId() != null && costs.existsByEventIdAndIdNot(request.eventId(), id)) {
            throw new ConflictException("eventId déjà utilisé : " + request.eventId());
        }
        return mapper.toResponse(mapper.update(entry, request));
    }

    // orphanRemoval : retiré de la liste de sa ligne, le coût est supprimé.
    @Override
    public void delete(UUID lineId, UUID id) {
        CostEntry entry = find(lineId, id);
        entry.getBudgetLine().removeCostEntry(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CostEntryResponse> findByProject(UUID projectId) {
        return costs.findByBudgetLineProjectId(projectId).stream().map(mapper::toResponse).toList();
    }

    private BudgetLine findLine(UUID lineId) {
        return lines.findById(lineId)
                .orElseThrow(() -> new NotFoundException("ligne de budget introuvable : " + lineId));
    }

    private CostEntry find(UUID lineId, UUID id) {
        findLine(lineId);
        return costs.findByIdAndBudgetLineId(id, lineId)
                .orElseThrow(() -> new NotFoundException("coût introuvable dans cette ligne : " + id));
    }
}
