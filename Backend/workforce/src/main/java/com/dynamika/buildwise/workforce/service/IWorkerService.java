package com.dynamika.buildwise.workforce.service;

import com.dynamika.buildwise.workforce.dto.WorkerRequest;
import com.dynamika.buildwise.workforce.dto.WorkerResponse;
import java.util.List;
import java.util.UUID;

public interface IWorkerService {
    List<WorkerResponse> findAll();
    WorkerResponse findById(UUID id);
    WorkerResponse create(WorkerRequest request);
    WorkerResponse update(UUID id, WorkerRequest request);
    void delete(UUID id);
}
