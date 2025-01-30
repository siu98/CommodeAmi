package com.siuuuuu.commodeami.averagescope.query.service;

import com.siuuuuu.commodeami.averagescope.query.aggregate.AverageScopeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AverageScopeServiceImplTests {

    @Autowired
    private AverageScopeService averageScopeService;

    @DisplayName("모든 평균별점 조회")
    @Test
    void findAllAverageScope() {
        // given

        // when
        List<AverageScopeDTO> averageScopeDTOList = averageScopeService.getAllAverageScopes();

        //then
        assertNotNull(averageScopeDTOList, "평균별점 목록이 null이 아닙니다.");
        assertFalse(averageScopeDTOList.isEmpty(), "평균별점 목록이 비어있지 않아야 합니다.");
        averageScopeDTOList.forEach(averageScopeDTO -> {
            log.info("averageScopeDTO: {}", averageScopeDTO);
        });

    }

    @DisplayName("특정 영화의 평균별점 조회")
    @Test
    void findAverageScopeByMovie() {
        // given
        Long movieId = 1L;

        // when
        AverageScopeDTO averageScopeDTO = averageScopeService.getAverageScopeByMovieId(movieId);

        // then
        assertNotNull(averageScopeDTO, "평균별점 목록이 null이 아닙니다.");
        assertEquals(movieId, averageScopeDTO.getMovieId(), "조회한 영화 ID가 일치해야 합니다.");

        log.info("영화 정보: {}", averageScopeDTO);
    }
}