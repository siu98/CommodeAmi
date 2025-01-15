package com.siuuuuu.commodeami.review.query.service;

import com.siuuuuu.commodeami.review.query.aggregate.ReviewDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getAllReviews();

    ReviewDTO getReviewByUserId(Long userId);

    ReviewDTO getReviewByUserIdAndMovieId(Long userId, Long movieId);
}
