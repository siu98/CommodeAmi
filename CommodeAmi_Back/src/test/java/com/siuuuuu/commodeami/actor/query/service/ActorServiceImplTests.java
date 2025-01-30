package com.siuuuuu.commodeami.actor.query.service;

import com.siuuuuu.commodeami.actor.query.aggregate.ActorDTO;
import com.siuuuuu.commodeami.actor.query.aggregate.MovieActorDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ActorServiceImplTests {

    @Autowired
    private ActorService actorService;

    @DisplayName("모든 배우 조회")
    @Test
    void findAllActors() {
        // given

        // when
        List<ActorDTO> actorDTOList = actorService.getAllActors();

        // then
        assertNotNull(actorDTOList, "배우 목록이 null이 아닙니다.");
        assertFalse(actorDTOList.isEmpty(), "배우 목록이 비어있지 않아야 합니다.");
        actorDTOList.forEach(actorDTO -> {
            log.info("actorDTO: {}", actorDTO);
        });

    }

    @DisplayName("특정 영화의 배우 조회")
    @Test
    void findActorsByMovie() {
        // given
        Long movieId = 1L;

        // when
        List<MovieActorDTO> movieActorList = actorService.getActorsByMovieId(movieId);

        // then
        assertNotNull(movieActorList, "배우 목록이 null이 아닙니다.");
        assertFalse(movieActorList.isEmpty(), "배우 목록이 비어있지 않아야 합니다.");

        movieActorList.forEach(actor -> {
            log.info("배우 정보: {}", actor);
        });

    }
}