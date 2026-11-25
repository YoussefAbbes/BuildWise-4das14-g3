package com.dynamika.buildwise.evaluation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dynamika.buildwise.evaluation.client.WorkforceClient;
import com.dynamika.buildwise.evaluation.dto.ReviewRequest;
import com.dynamika.buildwise.evaluation.dto.ReviewResponse;
import com.dynamika.buildwise.evaluation.dto.ReviewUpdateRequest;
import com.dynamika.buildwise.evaluation.dto.WorkerDto;
import com.dynamika.buildwise.evaluation.exception.ConflictException;
import com.dynamika.buildwise.evaluation.exception.NotFoundException;
import com.dynamika.buildwise.evaluation.mapper.EvaluationMapperImpl;
import com.dynamika.buildwise.evaluation.model.Review;
import com.dynamika.buildwise.evaluation.repository.ReviewRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Tests unitaires : workforce, MongoDB et le service des scores sont simulés par Mockito.
class ReviewServiceTest {
    private static final UUID WORKER = UUID.fromString("a0000000-0000-0000-0000-000000000001");
    private static final UUID TASK = UUID.fromString("e0000000-0000-0000-0000-000000000001");
    private static final UUID PROJECT = UUID.fromString("d0000000-0000-0000-0000-000000000001");
    private static final UUID AUTHOR = UUID.fromString("f0000000-0000-0000-0000-000000000001");

    private ReviewRepository reviews;
    private IScoreService scores;
    private WorkforceClient workforce;
    private ReviewService service;

    @BeforeEach
    void setUp() {
        reviews = mock(ReviewRepository.class);
        scores = mock(IScoreService.class);
        workforce = mock(WorkforceClient.class);
        service = new ReviewService(reviews, scores, workforce, new EvaluationMapperImpl());
        when(reviews.save(any(Review.class))).thenAnswer(call -> call.getArgument(0));
    }

    private static ReviewRequest request() {
        return new ReviewRequest(WORKER, TASK, PROJECT, 4, "bon travail", AUTHOR);
    }

    @Test
    void createSavesTheReviewAndRefreshesTheScoreWithTheWorkforceName() {
        when(workforce.worker(WORKER)).thenReturn(new WorkerDto(WORKER, "Ali Ben Salah"));

        ReviewResponse created = service.create(request());

        assertThat(created.id()).isNotNull();
        assertThat(created.createdAt()).isNotNull();
        assertThat(created.score()).isEqualTo(4);
        verify(scores).refresh(WORKER, "Ali Ben Salah");
    }

    @Test
    void createRefusesASecondReviewForTheSameTask() {
        when(workforce.worker(WORKER)).thenReturn(new WorkerDto(WORKER, "Ali Ben Salah"));
        when(reviews.existsByWorkerIdAndTaskId(WORKER, TASK)).thenReturn(true);

        assertThatThrownBy(() -> service.create(request())).isInstanceOf(ConflictException.class);
        verify(reviews, never()).save(any());
    }

    @Test
    void createStopsWhenWorkforceDoesNotKnowTheWorker() {
        // c'est ce que lève WorkforceFallbackFactory pour un 404 de workforce
        when(workforce.worker(WORKER)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "ouvrier inconnu"));

        assertThatThrownBy(() -> service.create(request()))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        verify(reviews, never()).save(any());
        verify(scores, never()).refresh(any(), any());
    }

    @Test
    void updateChangesOnlyScoreAndComment() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.parse("2026-11-25T10:00:00Z");
        when(reviews.findById(id)).thenReturn(Optional.of(
                new Review(id, WORKER, TASK, PROJECT, 2, "moyen", AUTHOR, createdAt)));

        ReviewResponse updated = service.update(id, new ReviewUpdateRequest(5, "très bon travail"));

        assertThat(updated.score()).isEqualTo(5);
        assertThat(updated.comment()).isEqualTo("très bon travail");
        assertThat(updated.id()).isEqualTo(id);
        assertThat(updated.workerId()).isEqualTo(WORKER);
        assertThat(updated.taskId()).isEqualTo(TASK);
        assertThat(updated.createdAt()).isEqualTo(createdAt);
        verify(scores).refresh(WORKER, null);
    }

    @Test
    void unknownReviewIsNotFound() {
        UUID id = UUID.randomUUID();
        when(reviews.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id)).isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> service.delete(id)).isInstanceOf(NotFoundException.class);
    }
}
