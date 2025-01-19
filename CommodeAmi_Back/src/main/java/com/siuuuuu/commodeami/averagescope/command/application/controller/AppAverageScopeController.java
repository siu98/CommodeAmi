package com.siuuuuu.commodeami.averagescope.command.application.controller;

import com.siuuuuu.commodeami.averagescope.command.application.service.AppAverageScopeService;
import com.siuuuuu.commodeami.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/average-scope")
public class AppAverageScopeController {

    private final AppAverageScopeService appAverageScopeService;

    @Autowired
    public AppAverageScopeController(AppAverageScopeService appAverageScopeService) {
        this.appAverageScopeService = appAverageScopeService;
    }

    // 특정 영화에 대한 평균 별점 계산 및 저장/업데이트
    @PostMapping("")
    public ResponseDTO<?> saveAverageScope(@RequestParam("movieId") Long movieId,
                                           @RequestParam("scope") Double scope,
                                           @RequestParam("userId") Long userId,
                                           @RequestParam("isUpdate") boolean isUpdate) {
        return ResponseDTO.ok(appAverageScopeService.saveOrUpdateAverageScope(movieId, scope, userId, isUpdate));
    }

}
