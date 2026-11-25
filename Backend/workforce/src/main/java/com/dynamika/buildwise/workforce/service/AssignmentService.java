package com.dynamika.buildwise.workforce.service;

import com.dynamika.buildwise.workforce.dto.AssignmentRequest;
import com.dynamika.buildwise.workforce.dto.AssignmentResponse;
import com.dynamika.buildwise.workforce.exception.NotFoundException;
import com.dynamika.buildwise.workforce.mapper.WorkforceMapper;
import com.dynamika.buildwise.workforce.model.Assignment;
import com.dynamika.buildwise.workforce.repository.AssignmentRepository;
import com.dynamika.buildwise.workforce.repository.WorkerRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AssignmentService implements IAssignmentService {
    private final AssignmentRepository assignments;
    private final WorkerRepository workers;
    private final WorkforceMapper mapper;

    public AssignmentService(AssignmentRepository assignments, WorkerRepository workers, WorkforceMapper mapper) {
        this.assignments = assignments;
        this.workers = workers;
        this.mapper = mapper;
    }

    @Override
    public List<AssignmentResponse> findAll(UUID workerId) {
        List<Assignment> found = workerId == null ? assignments.findAll() : assignments.findByWorkerId(workerId);
        return found.stream().map(mapper::toResponse).toList();
    }

    @Override
    public AssignmentResponse findById(UUID id) {
        return assignments.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("affectation introuvable : " + id));
    }

    @Override
    public AssignmentResponse create(AssignmentRequest request) {
        check(request);
        return mapper.toResponse(assignments.save(mapper.toModel(request)));
    }

    @Override
    public AssignmentResponse update(UUID id, AssignmentRequest request) {
        findById(id);
        check(request);
        return mapper.toResponse(assignments.save(mapper.toModel(id, request)));
    }

    @Override
    public void delete(UUID id) {
        findById(id);
        assignments.deleteById(id);
    }

    private void check(AssignmentRequest request) {
        if (!workers.existsById(request.workerId())) {
            throw new NotFoundException("ouvrier introuvable : " + request.workerId());
        }
        if (request.to().isBefore(request.from())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "la date to est avant la date from");
        }
    }
}
