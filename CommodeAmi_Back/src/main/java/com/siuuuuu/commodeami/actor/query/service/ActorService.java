package com.siuuuuu.commodeami.actor.query.service;

import com.siuuuuu.commodeami.actor.query.aggregate.ActorDTO;
import com.siuuuuu.commodeami.actor.query.aggregate.MovieActorDTO;

import java.util.List;

public interface ActorService {
    List<ActorDTO> getAllActors();

    List<MovieActorDTO> getActorsByMovieId(Long movieId);
}
