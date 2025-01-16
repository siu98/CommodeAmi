package com.siuuuuu.commodeami.review.command.aggregate.dto;

import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import com.siuuuuu.commodeami.user.command.aggregate.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long reviewId;
    private String review;
    private LocalDateTime createdAt;
    private Long movieId;
    private Long userId;
    private Long scopeId;
}
