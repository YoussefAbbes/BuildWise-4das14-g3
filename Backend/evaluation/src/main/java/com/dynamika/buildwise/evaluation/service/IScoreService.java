package com.dynamika.buildwise.evaluation.service;

import com.dynamika.buildwise.evaluation.dto.WorkerScoreResponse;
import java.util.List;
import java.util.UUID;

public interface IScoreService {
    List<WorkerScoreResponse> findAll();

    /** Ouvrier sans avis : moyenne 0 et nombre 0, jamais 404. */
    WorkerScoreResponse findByWorkerId(UUID workerId);

    /** Recalcule la note d'un ouvrier à partir de ses avis. fullName null : on garde le nom déjà enregistré. */
    void refresh(UUID workerId, String fullName);
}
