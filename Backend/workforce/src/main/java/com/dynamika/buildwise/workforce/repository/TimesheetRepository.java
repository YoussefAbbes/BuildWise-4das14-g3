package com.dynamika.buildwise.workforce.repository;

import com.dynamika.buildwise.workforce.model.Timesheet;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimesheetRepository extends MongoRepository<Timesheet, UUID> {
    List<Timesheet> findByWorkerId(UUID workerId);

    boolean existsByWorkerId(UUID workerId);
}
