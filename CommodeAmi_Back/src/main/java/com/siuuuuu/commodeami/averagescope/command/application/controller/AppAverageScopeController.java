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
    @PostMapping("")
    public ResponseDTO<?> saveAverageScope(@RequestParam("movieId") Long movieId,
                                           @RequestParam("scope") Double newScope,
                                           @RequestParam("oldScope") Double oldScope, // userId 대신 oldScope로 변경
                                           @RequestParam("isUpdate") boolean isUpdate) {
        return ResponseDTO.ok(appAverageScopeService.saveOrUpdateAverageScope(movieId, newScope, oldScope, isUpdate));
    }
}
