package com.dynamika.buildwise.evaluation.mapper;

import com.dynamika.buildwise.evaluation.dto.ReviewRequest;
import com.dynamika.buildwise.evaluation.dto.ReviewResponse;
import com.dynamika.buildwise.evaluation.dto.ReviewUpdateRequest;
import com.dynamika.buildwise.evaluation.dto.WorkerScoreResponse;
import com.dynamika.buildwise.evaluation.model.Review;
import com.dynamika.buildwise.evaluation.model.WorkerScore;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Convertit entre les documents (model) et les DTO.
 * MapStruct écrit le code à la compilation (EvaluationMapperImpl) : on déclare seulement les méthodes.
 * Spring l'injecte comme un bean. Classe abstraite : elle peut aussi contenir nos propres méthodes (newId).
 *
 * Création : le mapper génère l'id et la date de création est celle de l'appel.
 * Modification : seules la note et le commentaire changent, le reste vient de l'avis existant.
 */
@Mapper(componentModel = "spring")
public abstract class EvaluationMapper {

    // MongoDB ne génère pas les UUID : c'est le mapper qui le crée.
    protected UUID newId() {
        return UUID.randomUUID();
    }

    @Mapping(target = "id", expression = "java(newId())")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    public abstract Review toModel(ReviewRequest request);

    // score et comment existent des deux côtés : on prend ceux de la requête.
    @Mapping(source = "request.score", target = "score")
    @Mapping(source = "request.comment", target = "comment")
    public abstract Review toModel(Review review, ReviewUpdateRequest request);

    public abstract ReviewResponse toResponse(Review review);

    public abstract WorkerScoreResponse toResponse(WorkerScore score);
}
