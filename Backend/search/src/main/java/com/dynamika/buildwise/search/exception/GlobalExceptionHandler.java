package com.dynamika.buildwise.search.exception;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduit les exceptions levées par le service en réponses HTTP lisibles (JSON).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    ProblemDetail notFound(NotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 400 avec la liste des champs invalides : { "errors": { "name": "...", "id": "..." } }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail invalid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(f.getField(), f.getDefaultMessage());
        }
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "corps de requête invalide");
        p.setProperty("errors", errors);
        return p;
    }

    // Meilisearch éteint, injoignable ou en erreur : 503 au lieu d'une trace de 500.
    @ExceptionHandler(MeilisearchException.class)
    ProblemDetail meiliDown(MeilisearchException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, "Meilisearch ne répond pas");
    }
}
