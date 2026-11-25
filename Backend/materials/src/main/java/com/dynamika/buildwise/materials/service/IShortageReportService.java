package com.dynamika.buildwise.materials.service;

import com.dynamika.buildwise.materials.dto.ShortageReportRequest;
import com.dynamika.buildwise.materials.dto.ShortageReportResponse;
import java.util.List;
import java.util.UUID;

public interface IShortageReportService {
    /** siteStockId null : tous les signalements. */
    List<ShortageReportResponse> findAll(UUID siteStockId);

    ShortageReportResponse findById(UUID id);

    ShortageReportResponse create(ShortageReportRequest request);

    ShortageReportResponse update(UUID id, ShortageReportRequest request);

    void delete(UUID id);
}
