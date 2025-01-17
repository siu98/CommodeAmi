package com.siuuuuu.commodeami.review.query.service;

import com.siuuuuu.commodeami.review.query.aggregate.Review;
import com.siuuuuu.commodeami.review.query.aggregate.ReviewDTO;
import com.siuuuuu.commodeami.review.query.repository.ReviewMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewServiceImpl(ReviewMapper reviewMapper,
                             ModelMapper modelMapper) {
        this.reviewMapper = reviewMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewMapper.selectAllReviews();
        List<ReviewDTO> reviewDTOs =
                reviews.stream().map(review -> modelMapper.map(review, ReviewDTO.class)).collect(Collectors.toList());
        return reviewDTOs;
    }

    @Override
    public ReviewDTO getReviewByUserId(Long userId) {
        Review review = reviewMapper.selectReviewByUserId(userId);

        // 리뷰가 없을 시 예외처리
        if (review == null) {
            throw new IllegalArgumentException("리뷰를 찾을 수 없습니다.");
        }

        // entity -> DTO 변환
        return modelMapper.map(review, ReviewDTO.class);
    }

    @Override
    public ReviewDTO getReviewByUserIdAndMovieId(Long userId, Long movieId) {
        Review review = reviewMapper.selectReviewByUserIdAndMovieId(userId, movieId);

        if (review == null) {
            return null;
        }

        return modelMapper.map(review, ReviewDTO.class);
    }

    @Override
    public List<ReviewDTO> getReviewByMovieId(Long movieId) {
        List<Review> reviews = reviewMapper.selectReviewByMovieId(movieId);
        List<ReviewDTO> revieDTOs =
                reviews.stream().map(review -> modelMapper.map(review, ReviewDTO.class)).collect(Collectors.toList());
        return revieDTOs;
    }

}
