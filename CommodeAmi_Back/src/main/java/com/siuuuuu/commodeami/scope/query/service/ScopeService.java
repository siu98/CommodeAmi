package com.siuuuuu.commodeami.scope.query.service;

import com.siuuuuu.commodeami.scope.query.aggregate.ScopeDTO;

import java.util.List;

public interface ScopeService {
    List<ScopeDTO> getAllScopes();

    List<ScopeDTO> getScopeByUserId(Long userId);

    ScopeDTO getScopeByMovieId(Long movieId);

    ScopeDTO getScopeByUserIdAndMovieId(Long userId, Long movieId);
}
