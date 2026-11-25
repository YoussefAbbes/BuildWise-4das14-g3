package com.dynamika.buildwise.evaluation.service;

import com.dynamika.buildwise.evaluation.client.WorkforceClient;
import com.dynamika.buildwise.evaluation.dto.ReviewRequest;
import com.dynamika.buildwise.evaluation.dto.ReviewResponse;
import com.dynamika.buildwise.evaluation.dto.ReviewUpdateRequest;
import com.dynamika.buildwise.evaluation.dto.WorkerDto;
import com.dynamika.buildwise.evaluation.exception.ConflictException;
import com.dynamika.buildwise.evaluation.exception.NotFoundException;
import com.dynamika.buildwise.evaluation.mapper.EvaluationMapper;
import com.dynamika.buildwise.evaluation.model.Review;
import com.dynamika.buildwise.evaluation.repository.ReviewRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ReviewService implements IReviewService {
    private final ReviewRepository reviews;
    private final IScoreService scores;
    private final WorkforceClient workforce;
    private final EvaluationMapper mapper;

    public ReviewService(ReviewRepository reviews, IScoreService scores, WorkforceClient workforce, EvaluationMapper mapper) {
        this.reviews = reviews;
        this.scores = scores;
        this.workforce = workforce;
        this.mapper = mapper;
    }

    @Override
    public List<ReviewResponse> findAll(UUID workerId) {
        List<Review> found = workerId == null ? reviews.findAll() : reviews.findByWorkerId(workerId);
        return found.stream().map(mapper::toResponse).toList();
    }

    @Override
    public ReviewResponse findById(UUID id) {
        return mapper.toResponse(find(id));
    }

    // workforce d'abord : 404 si l'ouvrier n'existe pas, 503 si workforce est éteint.
    @Override
    public ReviewResponse create(ReviewRequest request) {
        WorkerDto worker = workforce.worker(request.workerId());
        if (reviews.existsByWorkerIdAndTaskId(request.workerId(), request.taskId())) {
            throw new ConflictException("cet ouvrier a déjà un avis pour cette tâche");
        }
        Review saved = reviews.save(mapper.toModel(request));
        scores.refresh(saved.workerId(), worker.fullName());
        return mapper.toResponse(saved);
    }

    @Override
    public ReviewResponse update(UUID id, ReviewUpdateRequest request) {
        Review saved = reviews.save(mapper.toModel(find(id), request));
        scores.refresh(saved.workerId(), null);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        Review review = find(id);
        reviews.deleteById(id);
        scores.refresh(review.workerId(), null);
    }

    private Review find(UUID id) {
        return reviews.findById(id).orElseThrow(() -> new NotFoundException("avis introuvable : " + id));
    }
}
