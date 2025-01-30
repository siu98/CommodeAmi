package com.siuuuuu.commodeami.review.query.service;

import com.siuuuuu.commodeami.review.query.aggregate.ReviewDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ReviewServiceImplTests {

    @Autowired
    private ReviewService reviewService;

    @DisplayName("모든 리뷰 조회")
    @Test
    void findAllReviews() {
        // given

        // when
        List<ReviewDTO> reviewDTOList = reviewService.getAllReviews();

        // then
        assertNotNull(reviewDTOList, "리뷰 목록이 null이 아닙니다.");
        assertFalse(reviewDTOList.isEmpty(), "리뷰 목록이 비어있지 않아야 합니다.");
        reviewDTOList.forEach(reviewDTO -> {
            log.info("reviewDTO: {}", reviewDTO);
        });
    }

    @DisplayName("특정 회원의 리뷰 조회")
    @Test
    void findReviewByUserId() {
        // given
        Long userId = 2L;

        // when
        List<ReviewDTO> reviewDTOList = reviewService.getReviewByUserId(userId);

        // then
        assertNotNull(reviewDTOList, "리뷰 목록이 null이 아닙니다.");
        assertFalse(reviewDTOList.isEmpty(), "리뷰 목록이 비어있지 않아야 합니다.");
        reviewDTOList.forEach(reviewDTO -> {
            log.info("reviewDTO: {}", reviewDTO);
        });
    }

    @DisplayName("특정 회원의 특정 영화 리뷰 조회")
    @Test
    void findReviewByUserIdAndMovieId() {
        // given
        Long userId = 2L;
        Long movieId = 4L;

        // when
        ReviewDTO reviewDTO = reviewService.getReviewByUserIdAndMovieId(userId, movieId);

        // then
        assertNotNull(reviewDTO, "리뷰가 null이 아닙니다.");
        assertEquals(userId, reviewDTO.getUserId(), "유저 ID가 일치해야 합니다.");
        assertEquals(movieId, reviewDTO.getMovieId(), "영화 ID가 일치해야 합니다.");

        log.info("조회된 리뷰 정보: {}", reviewDTO);
    }

    @DisplayName("해당 영화의 리뷰 전체 조회")
    @Test
    void findReviewByMovieId() {
        // given
        Long movieId = 8L;

        // when
        List<ReviewDTO> reviewDTOList = reviewService.getReviewByMovieId(movieId);

        // then
        assertNotNull(reviewDTOList, "리뷰가 null이 아닙니다.");
        assertFalse(reviewDTOList.isEmpty(), "리뷰 목록이 비어있지 않아야 합니다.");

        // 🔥 각 리뷰의 movieId가 요청한 movieId와 일치하는지 확인
        reviewDTOList.forEach(review ->
                assertEquals(movieId, review.getMovieId(), "리뷰의 영화 ID가 요청한 영화 ID와 일치해야 합니다.")
        );

        log.info("조회된 리뷰 정보: {}", reviewDTOList);
    }
}