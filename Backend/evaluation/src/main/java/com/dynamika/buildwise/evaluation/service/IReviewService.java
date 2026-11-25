package com.dynamika.buildwise.evaluation.service;

import com.dynamika.buildwise.evaluation.dto.ReviewRequest;
import com.dynamika.buildwise.evaluation.dto.ReviewResponse;
import com.dynamika.buildwise.evaluation.dto.ReviewUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface IReviewService {
    /** workerId null : tous les avis. */
    List<ReviewResponse> findAll(UUID workerId);

    ReviewResponse findById(UUID id);

    ReviewResponse create(ReviewRequest request);

    ReviewResponse update(UUID id, ReviewUpdateRequest request);

    void delete(UUID id);
}
