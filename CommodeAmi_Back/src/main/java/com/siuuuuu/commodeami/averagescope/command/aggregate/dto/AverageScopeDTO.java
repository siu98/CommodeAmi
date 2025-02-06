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

}
