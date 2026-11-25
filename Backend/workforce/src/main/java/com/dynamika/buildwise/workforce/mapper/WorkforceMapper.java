package com.dynamika.buildwise.workforce.mapper;

import com.dynamika.buildwise.workforce.dto.AssignmentRequest;
import com.dynamika.buildwise.workforce.dto.AssignmentResponse;
import com.dynamika.buildwise.workforce.dto.TimesheetRequest;
import com.dynamika.buildwise.workforce.dto.TimesheetResponse;
import com.dynamika.buildwise.workforce.dto.WorkerRequest;
import com.dynamika.buildwise.workforce.dto.WorkerResponse;
import com.dynamika.buildwise.workforce.model.Assignment;
import com.dynamika.buildwise.workforce.model.Timesheet;
import com.dynamika.buildwise.workforce.model.Worker;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class WorkforceMapper {
    protected UUID newId() {
        return UUID.randomUUID();
    }

    @Mapping(target = "id", expression = "java(newId())")
    @Mapping(target = "skills", defaultExpression = "java(java.util.Set.of())")
    public abstract Worker toModel(WorkerRequest request);

    @Mapping(target = "skills", defaultExpression = "java(java.util.Set.of())")
    public abstract Worker toModel(UUID id, WorkerRequest request);

    public abstract WorkerResponse toResponse(Worker worker);

    @Mapping(target = "id", expression = "java(newId())")
    public abstract Assignment toModel(AssignmentRequest request);

    public abstract Assignment toModel(UUID id, AssignmentRequest request);

    public abstract AssignmentResponse toResponse(Assignment assignment);

    @Mapping(target = "id", expression = "java(newId())")
    public abstract Timesheet toModel(TimesheetRequest request);

    public abstract Timesheet toModel(UUID id, TimesheetRequest request);

    public abstract TimesheetResponse toResponse(Timesheet timesheet);
}
