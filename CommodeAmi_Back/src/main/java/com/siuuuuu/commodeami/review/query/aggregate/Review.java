package com.siuuuuu.commodeami.review.query.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    Long ReviewId;
    LocalDateTime createdAt;
    String review;
    Long userId;
    Long movieId;
    String nickname;
    Double scope;
    String title;
}
