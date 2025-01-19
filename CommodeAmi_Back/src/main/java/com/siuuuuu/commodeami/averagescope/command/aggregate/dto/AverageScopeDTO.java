package com.siuuuuu.commodeami.averagescope.command.aggregate.dto;

import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AverageScopeDTO {

    private Long averageScopeId;
    private Double averageScope;
    private Integer numberOfPeople;
    private Long movieId;
//    private Long userId;

    // 네 개의 파라미터를 받는 생성자
//    public AverageScopeDTO(Long averageScopeId, Double averageScope, Integer numberOfPeople, Long movieId) {
//        this.averageScopeId = averageScopeId;
//        this.averageScope = averageScope;
//        this.numberOfPeople = numberOfPeople;
//        this.movieId = movieId;
//    }

}
