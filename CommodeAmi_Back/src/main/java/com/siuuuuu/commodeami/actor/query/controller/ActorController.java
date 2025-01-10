package com.siuuuuu.commodeami.actor.query.controller;

import com.siuuuuu.commodeami.actor.query.service.ActorService;
import com.siuuuuu.commodeami.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/actor")
public class ActorController {

    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    // 모든 배우 조회
    @GetMapping("")
    public ResponseDTO<?> findAllActors() {
        return ResponseDTO.ok(actorService.getAllActors());
    }

    // 특정 영화의 배우 조회
    @GetMapping("/{movieId}")
    public ResponseDTO<?> findActorByMovieId(@PathVariable("movieId") Long movieId) {
        return ResponseDTO.ok(actorService.getActorsByMovieId(movieId));
    }
}
