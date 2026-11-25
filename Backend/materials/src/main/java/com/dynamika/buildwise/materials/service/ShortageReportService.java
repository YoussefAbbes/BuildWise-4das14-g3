package com.dynamika.buildwise.materials.service;

import com.dynamika.buildwise.materials.dto.ShortageReportRequest;
import com.dynamika.buildwise.materials.dto.ShortageReportResponse;
import com.dynamika.buildwise.materials.exception.NotFoundException;
import com.dynamika.buildwise.materials.mapper.MaterialsMapper;
import com.dynamika.buildwise.materials.model.ShortageReport;
import com.dynamika.buildwise.materials.repository.ShortageReportRepository;
import com.dynamika.buildwise.materials.repository.SiteStockRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ShortageReportService implements IShortageReportService {
    private final ShortageReportRepository shortages;
    private final SiteStockRepository stocks;
    private final MaterialsMapper mapper;

    public ShortageReportService(ShortageReportRepository shortages, SiteStockRepository stocks,
                                 MaterialsMapper mapper) {
        this.shortages = shortages;
        this.stocks = stocks;
        this.mapper = mapper;
    }

    @Override
    public List<ShortageReportResponse> findAll(UUID siteStockId) {
        List<ShortageReport> found = siteStockId == null ? shortages.findAll() : shortages.findBySiteStockId(siteStockId);
        return found.stream().map(mapper::toResponse).toList();
    }

    @Override
    public ShortageReportResponse findById(UUID id) {
        return mapper.toResponse(find(id));
    }

    // Le mapper crée l'id et met l'heure de création dans reportedAt.
    @Override
    public ShortageReportResponse create(ShortageReportRequest request) {
        checkSiteStock(request.siteStockId());
        return mapper.toResponse(shortages.save(mapper.toModel(request)));
    }

    // La date du signalement ne change pas à la modification.
    @Override
    public ShortageReportResponse update(UUID id, ShortageReportRequest request) {
        ShortageReport old = find(id);
        checkSiteStock(request.siteStockId());
        return mapper.toResponse(shortages.save(mapper.toModel(id, request, old.reportedAt())));
    }

    @Override
    public void delete(UUID id) {
        find(id);
        shortages.deleteById(id);
    }

    private ShortageReport find(UUID id) {
        return shortages.findById(id)
                .orElseThrow(() -> new NotFoundException("signalement de pénurie introuvable : " + id));
    }

    private void checkSiteStock(UUID siteStockId) {
        if (!stocks.existsById(siteStockId)) {
            throw new NotFoundException("stock de chantier introuvable : " + siteStockId);
        }
    }
}
