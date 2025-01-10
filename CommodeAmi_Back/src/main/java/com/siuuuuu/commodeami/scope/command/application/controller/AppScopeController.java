package com.siuuuuu.commodeami.scope.command.application.controller;

import com.siuuuuu.commodeami.common.ResponseDTO;
import com.siuuuuu.commodeami.scope.command.aggregate.dto.ScopeDTO;
import com.siuuuuu.commodeami.scope.command.application.service.AppScopeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/scope")
public class AppScopeController {

    private final AppScopeService appScopeService;

    @Autowired
    public AppScopeController(AppScopeService appScopeService) {
        this.appScopeService = appScopeService;
    }

    // 별점 추가 및 수정
    @PostMapping("/{movieId}/{userId}")
    public ResponseDTO<?> createOrUpdateScope(@PathVariable("movieId") Long movieId,
                                              @PathVariable("userId") Long userId,
                                              @RequestBody ScopeDTO scopeDTO) {
        log.info("별점 저장을 위한 userId={}, movieId={}", userId, movieId); // 디버깅 로그
        return ResponseDTO.ok(appScopeService.createOrUpdateScope(movieId, userId, scopeDTO));
    }

    // 별점 삭제
    @DeleteMapping("/{scopeId}")
    public ResponseDTO<?> deleteScope(@PathVariable("scopeId") Long scopeId) {
        return ResponseDTO.ok(appScopeService.deleteScope(scopeId));
    }

    @PostMapping("/watched-at/{scopeId}")
    public ResponseDTO<?> createOrUpdateWatchedAt(@PathVariable("scopeId") Long scopeId,
                                                  @RequestBody ScopeDTO scopeDTO) {
        return ResponseDTO.ok(appScopeService.createOrUpdateWatchedAt(scopeId, scopeDTO));
    }

    @DeleteMapping("/watched-at/{scopeId}")
    public ResponseDTO<?> deleteWatchedAt(@PathVariable("scopeId") Long scopeId) {
        return ResponseDTO.ok(appScopeService.deleteWatchedAt(scopeId));
    }
}

