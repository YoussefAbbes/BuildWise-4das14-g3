package com.dynamika.buildwise.workforce.service;

import com.dynamika.buildwise.workforce.dto.WorkerRequest;
import com.dynamika.buildwise.workforce.dto.WorkerResponse;
import com.dynamika.buildwise.workforce.exception.ConflictException;
import com.dynamika.buildwise.workforce.exception.NotFoundException;
import com.dynamika.buildwise.workforce.mapper.WorkforceMapper;
import com.dynamika.buildwise.workforce.repository.AssignmentRepository;
import com.dynamika.buildwise.workforce.repository.TimesheetRepository;
import com.dynamika.buildwise.workforce.repository.WorkerRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class WorkerService implements IWorkerService {
    private final WorkerRepository workers;
    private final AssignmentRepository assignments;
    private final TimesheetRepository timesheets;
    private final WorkforceMapper mapper;

    public WorkerService(WorkerRepository workers, AssignmentRepository assignments,
                         TimesheetRepository timesheets, WorkforceMapper mapper) {
        this.workers = workers;
        this.assignments = assignments;
        this.timesheets = timesheets;
        this.mapper = mapper;
    }

    @Override
    public List<WorkerResponse> findAll() {
        return workers.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public WorkerResponse findById(UUID id) {
        return workers.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("ouvrier introuvable : " + id));
    }

    @Override
    public WorkerResponse create(WorkerRequest request) {
        return mapper.toResponse(workers.save(mapper.toModel(request)));
    }

    @Override
    public WorkerResponse update(UUID id, WorkerRequest request) {
        findById(id);
        return mapper.toResponse(workers.save(mapper.toModel(id, request)));
    }

    @Override
    public void delete(UUID id) {
        findById(id);
        if (assignments.existsByWorkerId(id) || timesheets.existsByWorkerId(id)) {
            throw new ConflictException("cet ouvrier a encore des affectations ou des pointages");
        }
        workers.deleteById(id);
    }
}
