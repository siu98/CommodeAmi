package com.siuuuuu.commodeami.review.command.application.controller;

import com.siuuuuu.commodeami.common.ResponseDTO;
import com.siuuuuu.commodeami.review.command.aggregate.dto.ReviewDTO;
import com.siuuuuu.commodeami.review.command.application.service.AppReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/review")
public class AppReviewController {

    private AppReviewService appReviewService;

    @Autowired
    public AppReviewController(AppReviewService appReviewService) {
        this.appReviewService = appReviewService;
    }

    // 리뷰 추가
    @PostMapping("/{movieId}/{userId}")
    public ResponseDTO<?> createReview(@PathVariable("movieId") Long movieId,
                                               @PathVariable("userId") Long userId,
                                               @RequestBody ReviewDTO reviewDTO) {
        return ResponseDTO.ok(appReviewService.createReview(movieId, userId, reviewDTO));
    }


    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseDTO<?> deleteReview(@PathVariable("reviewId") Long reviewId) {
        return ResponseDTO.ok(appReviewService.deleteReview(reviewId));
    }
}
