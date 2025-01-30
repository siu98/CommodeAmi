package com.siuuuuu.commodeami.averagescope.query.controller;

import com.siuuuuu.commodeami.averagescope.query.service.AverageScopeService;
import com.siuuuuu.commodeami.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/average-scope")
public class AverageScopeController {

    private final AverageScopeService averageScopeService;

    @Autowired
    public AverageScopeController(AverageScopeService averageScopeService) {
        this.averageScopeService = averageScopeService;
    }

    // 모든 평균 별점 조회
    @GetMapping("")
    public ResponseDTO<?> findAllAverageScope() {
        return ResponseDTO.ok(averageScopeService.getAllAverageScopes());
    }

    // 해당 영화의 평균별점 조회
    @GetMapping("/{movieId}")
    public ResponseDTO<?> findAverageScopeByMovieId(@PathVariable("movieId") Long movieId) {
        return ResponseDTO.ok(averageScopeService.getAverageScopeByMovieId(movieId));
    }

}
