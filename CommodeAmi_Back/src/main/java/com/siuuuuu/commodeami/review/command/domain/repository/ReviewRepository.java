package com.siuuuuu.commodeami.review.command.domain.repository;

import com.siuuuuu.commodeami.review.command.aggregate.dto.ReviewDTO;
import com.siuuuuu.commodeami.review.command.aggregate.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId AND r.movie.movieId = :movieId")
    Review findReviewByUserIdAndMovieId(Long userId, Long movieId);
}
