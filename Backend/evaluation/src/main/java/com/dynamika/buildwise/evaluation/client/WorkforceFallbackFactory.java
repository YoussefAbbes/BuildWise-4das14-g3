package com.dynamika.buildwise.evaluation.client;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class WorkforceFallbackFactory implements FallbackFactory<WorkforceClient> {
    private static final Logger log = LoggerFactory.getLogger(WorkforceFallbackFactory.class);

    @Override
    public WorkforceClient create(Throwable cause) {
        return id -> {
            if (cause instanceof FeignException.NotFound) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ouvrier inconnu : " + id);
            }
            // la vraie raison du 503 (délai dépassé, disjoncteur ouvert, workforce injoignable...) reste dans le log
            log.warn("workforce indisponible pour l'ouvrier {} : {}", id, cause.toString());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "workforce indisponible");
        };
    }
}
