package com.siuuuuu.commodeami.scope.query.controller;

import com.siuuuuu.commodeami.common.ResponseDTO;
import com.siuuuuu.commodeami.scope.query.service.ScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/scope")
public class ScopeController {

    private final ScopeService scopeService;

    @Autowired
    public ScopeController(ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    // 모든 별점 조회
    @GetMapping("")
    public ResponseDTO<?> findAllScopes() {
        return ResponseDTO.ok(scopeService.getAllScopes());
    }

    // 해당 유저의 별점 모두 조회
    @GetMapping("/user/{userId}")
    public ResponseDTO<?> findScopeByUserId(@PathVariable("userId") Long userId) {
        return ResponseDTO.ok(scopeService.getScopeByUserId(userId));
    }

    // 해당 영화의 별점 모두 조회
    @GetMapping("/movie/{movieId}")
    public ResponseDTO<?> findScopeByMovieId(@PathVariable("movieId") Long movieId) {
        return ResponseDTO.ok(scopeService.getScopeByMovieId(movieId));
    }

    // 해당 유저의 특정 영화의 별점 조회
    @GetMapping("{userId}/{movieId}")
    public ResponseDTO<?> findScopeByUserIdAndMovieId(@PathVariable("userId") Long userId,
                                                      @PathVariable("movieId") Long movieId) {
        return ResponseDTO.ok(scopeService.getScopeByUserIdAndMovieId(userId, movieId));
    }

}
