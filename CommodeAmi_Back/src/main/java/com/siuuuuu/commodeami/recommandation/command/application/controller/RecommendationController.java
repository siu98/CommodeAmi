package com.siuuuuu.commodeami.recommandation.command.application.controller;

import com.siuuuuu.commodeami.common.ResponseDTO;
import com.siuuuuu.commodeami.common.exception.CommonException;
import com.siuuuuu.commodeami.common.exception.ErrorCode;
import com.siuuuuu.commodeami.common.exception.ExceptionDTO;
import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import com.siuuuuu.commodeami.recommandation.command.application.service.RecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("")
    public ResponseDTO<List<Movie>> recommendMovies(
            @RequestParam double lat, @RequestParam double lon) {
        log.info("추천 영화 요청: lat={}, lon={}", lat, lon); // 디버깅 로그
        try {
            return ResponseDTO.ok(recommendationService.recommendMovies(lat, lon));
        } catch (CommonException e) {
            return new ResponseDTO<>(
                    e.getErrorCode().getHttpStatus(),
                    false,
                    null,
                    ExceptionDTO.of(e.getErrorCode())
            ); // ✅ 반환 타입을 명확히 지정
        } catch (Exception e) {
            return new ResponseDTO<>(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    null,
                    ExceptionDTO.of(ErrorCode.NOT_FOUND_USER) // ✅ ErrorCode만 사용
            );
        }
    }
}
