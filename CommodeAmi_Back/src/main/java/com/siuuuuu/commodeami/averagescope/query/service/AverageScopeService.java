package com.siuuuuu.commodeami.averagescope.query.service;

import com.siuuuuu.commodeami.averagescope.query.aggregate.AverageScopeDTO;

import java.util.List;

public interface AverageScopeService {

    List<AverageScopeDTO> getAllAverageScopes();

    AverageScopeDTO getAverageScopeByMovieId(Long movieId);

}
