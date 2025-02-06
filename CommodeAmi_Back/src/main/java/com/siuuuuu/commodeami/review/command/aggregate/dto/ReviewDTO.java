package com.siuuuuu.commodeami.review.command.aggregate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

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
