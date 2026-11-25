package com.dynamika.buildwise.workforce.service;

import com.dynamika.buildwise.workforce.dto.TimesheetRequest;
import com.dynamika.buildwise.workforce.dto.TimesheetResponse;
import java.util.List;
import java.util.UUID;

public interface ITimesheetService {
    List<TimesheetResponse> findAll(UUID workerId);
    TimesheetResponse findById(UUID id);
    TimesheetResponse create(TimesheetRequest request);
    TimesheetResponse update(UUID id, TimesheetRequest request);
    void delete(UUID id);
}
