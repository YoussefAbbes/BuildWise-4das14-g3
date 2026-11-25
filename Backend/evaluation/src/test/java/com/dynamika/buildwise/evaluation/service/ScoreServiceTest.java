package com.dynamika.buildwise.evaluation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dynamika.buildwise.evaluation.dto.WorkerScoreResponse;
import com.dynamika.buildwise.evaluation.mapper.EvaluationMapperImpl;
import com.dynamika.buildwise.evaluation.model.Review;
import com.dynamika.buildwise.evaluation.model.WorkerScore;
import com.dynamika.buildwise.evaluation.repository.ReviewRepository;
import com.dynamika.buildwise.evaluation.repository.WorkerScoreRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

// Tests unitaires : pas de Spring, pas de MongoDB (dépôts simulés par Mockito).
class ScoreServiceTest {
    private static final UUID WORKER = UUID.fromString("a0000000-0000-0000-0000-000000000001");

    private WorkerScoreRepository scores;
    private ReviewRepository reviews;
    private ScoreService service;

    @BeforeEach
    void setUp() {
        scores = mock(WorkerScoreRepository.class);
        reviews = mock(ReviewRepository.class);
        service = new ScoreService(scores, reviews, new EvaluationMapperImpl());
    }

    private static Review review(int score) {
        return new Review(UUID.randomUUID(), WORKER, UUID.randomUUID(), UUID.randomUUID(), score, null,
                UUID.randomUUID(), Instant.now());
    }

    @Test
    void refreshComputesAverageWithTwoDecimals() {
        when(reviews.findByWorkerId(WORKER)).thenReturn(List.of(review(4), review(5), review(5)));

        service.refresh(WORKER, "Ali Ben Salah");

        ArgumentCaptor<WorkerScore> saved = ArgumentCaptor.forClass(WorkerScore.class);
        verify(scores).save(saved.capture());
        assertThat(saved.getValue().average()).isEqualByComparingTo("4.67");   // 14 / 3, arrondi HALF_UP
        assertThat(saved.getValue().count()).isEqualTo(3);
        assertThat(saved.getValue().fullName()).isEqualTo("Ali Ben Salah");
    }

    @Test
    void refreshWithoutNameKeepsTheStoredName() {
        when(reviews.findByWorkerId(WORKER)).thenReturn(List.of(review(3)));
        when(scores.findById(WORKER)).thenReturn(Optional.of(new WorkerScore(WORKER, "Ali Ben Salah", BigDecimal.ONE, 1)));

        service.refresh(WORKER, null);

        ArgumentCaptor<WorkerScore> saved = ArgumentCaptor.forClass(WorkerScore.class);
        verify(scores).save(saved.capture());
        assertThat(saved.getValue().fullName()).isEqualTo("Ali Ben Salah");
    }

    @Test
    void refreshDeletesTheScoreWhenNoReviewIsLeft() {
        when(reviews.findByWorkerId(WORKER)).thenReturn(List.of());

        service.refresh(WORKER, null);

        verify(scores).deleteById(WORKER);
        verify(scores, never()).save(any());
    }

    @Test
    void workerWithoutReviewsHasZeroAverage() {
        when(scores.findById(WORKER)).thenReturn(Optional.empty());

        WorkerScoreResponse score = service.findByWorkerId(WORKER);

        assertThat(score.average()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(score.count()).isZero();
    }
}
