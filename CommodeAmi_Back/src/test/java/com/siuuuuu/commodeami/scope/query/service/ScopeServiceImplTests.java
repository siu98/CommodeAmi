package com.siuuuuu.commodeami.scope.query.service;

import com.siuuuuu.commodeami.scope.query.aggregate.ScopeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ScopeServiceImplTests {

    @Autowired
    private ScopeService scopeService;

    @DisplayName("모든 별점 조회")
    @Test
    void findAllScopes() {
        // given

        // when
        List<ScopeDTO> scopeDTOList = scopeService.getAllScopes();

         // then
        assertNotNull(scopeDTOList, "별점 목록이 null이 아닙니다.");
        assertFalse(scopeDTOList.isEmpty(), "별점 목록이 비어있지 않아야 합니다.");
        scopeDTOList.forEach(scopeDTO -> {
            log.info("scopeDTO: {}", scopeDTO);
        });
    }

    @DisplayName("특정 유저의 별점 조회")
    @Test
    void findScopesByUserId() {
        // given
        Long userId = 2L;

        // when
        List<ScopeDTO> scopeDTOList = scopeService.getScopeByUserId(userId);

        // then
        assertNotNull(scopeDTOList, "별점 목록이 null이 아닙니다.");
        assertFalse(scopeDTOList.isEmpty(), "별점 목록이 비어있지 않아야 합니다.");
        scopeDTOList.forEach(scopeDTO -> {
            log.info("scopeDTO: {}", scopeDTO);
        });

    }

//    @DisplayName("특정 영화의 별점 조회")
//    @Test
//    void findScopesByMovieId() {
//        // given
//        Long movieId = 8L;
//
//        // when
//        ScopeDTO scopeDTO = scopeService.getScopeByMovieId(movieId);
//
//        // then
//        assertNotNull(scopeDTO, "별점이 null이 아닙니다.");
//        assertEquals(movieId, scopeDTO.getMovieId(), "영화 ID가 일치해야 합니다.");
//        log.info("조회된 별점 정보: {}", scopeDTO);
//    }

    @DisplayName("특정 유저의 특정 영화 별점 조회")
    @Test
    void findScopeByUserIdAndMovieId() {
        // given
        Long userId = 2L;
        Long movieId = 8L;

        // when
        ScopeDTO scopeDTO = scopeService.getScopeByUserIdAndMovieId(userId, movieId);

        // then
        assertNotNull(scopeDTO, "별점이 null이 아닙니다.");
        assertEquals(userId, scopeDTO.getUserId(), "유저 ID가 일치해야 합니다.");
        assertEquals(movieId, scopeDTO.getMovieId(), "영화 ID가 일치해야 합니다.");

        log.info("조회된 별점 정보: {}", scopeDTO);
    }
}