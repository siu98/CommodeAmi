package com.siuuuuu.commodeami.review.query.repository;

import com.siuuuuu.commodeami.review.query.aggregate.Review;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

    List<Review> selectAllReviews();

    List<Review> selectReviewByUserId(@Param("userId") Long userId);

    Review selectReviewByUserIdAndMovieId(@Param("userId") Long userId, @Param("movieId") Long movieId);

    List<Review> selectReviewByMovieId(@Param("movieId") Long movieId);
}
