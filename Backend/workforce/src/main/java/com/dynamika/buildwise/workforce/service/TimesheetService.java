package com.dynamika.buildwise.workforce.service;

import com.dynamika.buildwise.workforce.dto.TimesheetRequest;
import com.dynamika.buildwise.workforce.dto.TimesheetResponse;
import com.dynamika.buildwise.workforce.exception.NotFoundException;
import com.dynamika.buildwise.workforce.mapper.WorkforceMapper;
import com.dynamika.buildwise.workforce.model.Timesheet;
import com.dynamika.buildwise.workforce.repository.TimesheetRepository;
import com.dynamika.buildwise.workforce.repository.WorkerRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TimesheetService implements ITimesheetService {
    private final TimesheetRepository timesheets;
    private final WorkerRepository workers;
    private final WorkforceMapper mapper;

    public TimesheetService(TimesheetRepository timesheets, WorkerRepository workers, WorkforceMapper mapper) {
        this.timesheets = timesheets;
        this.workers = workers;
        this.mapper = mapper;
    }

    @Override
    public List<TimesheetResponse> findAll(UUID workerId) {
        List<Timesheet> found = workerId == null ? timesheets.findAll() : timesheets.findByWorkerId(workerId);
        return found.stream().map(mapper::toResponse).toList();
    }

    @Override
    public TimesheetResponse findById(UUID id) {
        return timesheets.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("pointage introuvable : " + id));
    }

    @Override
    public TimesheetResponse create(TimesheetRequest request) {
        checkWorker(request.workerId());
        return mapper.toResponse(timesheets.save(mapper.toModel(request)));
    }

    @Override
    public TimesheetResponse update(UUID id, TimesheetRequest request) {
        findById(id);
        checkWorker(request.workerId());
        return mapper.toResponse(timesheets.save(mapper.toModel(id, request)));
    }

    @Override
    public void delete(UUID id) {
        findById(id);
        timesheets.deleteById(id);
    }

    private void checkWorker(UUID workerId) {
        if (!workers.existsById(workerId)) {
            throw new NotFoundException("ouvrier introuvable : " + workerId);
        }
    }
}
