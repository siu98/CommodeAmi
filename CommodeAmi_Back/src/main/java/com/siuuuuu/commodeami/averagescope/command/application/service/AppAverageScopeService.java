package com.siuuuuu.commodeami.averagescope.command.application.service;


import com.siuuuuu.commodeami.averagescope.command.aggregate.dto.AverageScopeDTO;

public interface AppAverageScopeService {

    AverageScopeDTO saveOrUpdateAverageScope(Long movieId, Double newScope, Double oldScope, boolean isUpdate); // userId 대신 oldScope 사용

}
