package com.siuuuuu.commodeami.averagescope.command.application.service;


import com.siuuuuu.commodeami.averagescope.command.aggregate.dto.AverageScopeDTO;

public interface AppAverageScopeService {

    AverageScopeDTO saveOrUpdateAverageScope(Long movieId, Double scope, Long userId, boolean isUpdate);
}
