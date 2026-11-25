package com.dynamika.buildwise.evaluation.client;

import com.dynamika.buildwise.evaluation.dto.WorkerDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Disjoncteur Resilience4j nommé d'après cette méthode : WorkforceClientworkerUUID.
@FeignClient(name = "workforce", fallbackFactory = WorkforceFallbackFactory.class)
public interface WorkforceClient {
    @GetMapping("/workers/{id}")
    WorkerDto worker(@PathVariable("id") UUID id);
}
