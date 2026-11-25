package com.dynamika.buildwise.evaluation.service;

import com.dynamika.buildwise.evaluation.dto.WorkerScoreResponse;
import com.dynamika.buildwise.evaluation.mapper.EvaluationMapper;
import com.dynamika.buildwise.evaluation.model.Review;
import com.dynamika.buildwise.evaluation.model.WorkerScore;
import com.dynamika.buildwise.evaluation.repository.ReviewRepository;
import com.dynamika.buildwise.evaluation.repository.WorkerScoreRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ScoreService implements IScoreService {
    private final WorkerScoreRepository scores;
    private final ReviewRepository reviews;
    private final EvaluationMapper mapper;

    public ScoreService(WorkerScoreRepository scores, ReviewRepository reviews, EvaluationMapper mapper) {
        this.scores = scores;
        this.reviews = reviews;
        this.mapper = mapper;
    }

    @Override
    public List<WorkerScoreResponse> findAll() {
        return scores.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public WorkerScoreResponse findByWorkerId(UUID workerId) {
        return scores.findById(workerId).map(mapper::toResponse)
                .orElse(new WorkerScoreResponse(workerId, null, BigDecimal.ZERO, 0));
    }

    // Moyenne à 2 décimales ; plus aucun avis : le document est supprimé.
    @Override
    public void refresh(UUID workerId, String fullName) {
        List<Review> all = reviews.findByWorkerId(workerId);
        if (all.isEmpty()) {
            scores.deleteById(workerId);
            return;
        }
        int sum = all.stream().mapToInt(Review::score).sum();
        BigDecimal average = BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(all.size()), 2, RoundingMode.HALF_UP);
        String name = fullName != null ? fullName
                : scores.findById(workerId).map(WorkerScore::fullName).orElse(null);
        scores.save(new WorkerScore(workerId, name, average, all.size()));
    }
}
