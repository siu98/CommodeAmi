package com.siuuuuu.commodeami.review.query.controller;

import com.siuuuuu.commodeami.common.ResponseDTO;

import com.siuuuuu.commodeami.review.query.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 모든 리뷰 조회
    @GetMapping("")
    public ResponseDTO<?> findAllReviews() {
        return ResponseDTO.ok(reviewService.getAllReviews());
    }

    // 해당 유저의 리뷰 모두 조회
    @GetMapping("{userId}")
    public ResponseDTO<?> findAllReviewsByUserId(@PathVariable("userId") Long userId) {
        return ResponseDTO.ok(reviewService.getReviewByUserId(userId));
    }

    // 해당 유저의 특정 영화의 리뷰 조회
    @GetMapping("{userId}/{movieId}")
    public ResponseDTO<?> findReviewByUserIdAndMovieId(@PathVariable("userId") Long userId,
                                                       @PathVariable("movieId") Long movieId) {
        return ResponseDTO.ok(reviewService.getReviewByUserIdAndMovieId(userId, movieId));
    }
}
