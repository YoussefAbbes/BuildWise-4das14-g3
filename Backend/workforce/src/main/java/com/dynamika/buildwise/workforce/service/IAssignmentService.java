package com.dynamika.buildwise.workforce.service;

import com.dynamika.buildwise.workforce.dto.AssignmentRequest;
import com.dynamika.buildwise.workforce.dto.AssignmentResponse;
import java.util.List;
import java.util.UUID;

public interface IAssignmentService {
    List<AssignmentResponse> findAll(UUID workerId);
    AssignmentResponse findById(UUID id);
    AssignmentResponse create(AssignmentRequest request);
    AssignmentResponse update(UUID id, AssignmentRequest request);
    void delete(UUID id);
}
