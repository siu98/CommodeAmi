package com.siuuuuu.commodeami.actor.query.service;

import com.siuuuuu.commodeami.actor.query.aggregate.Actor;
import com.siuuuuu.commodeami.actor.query.aggregate.ActorDTO;
import com.siuuuuu.commodeami.actor.query.aggregate.MovieActor;
import com.siuuuuu.commodeami.actor.query.aggregate.MovieActorDTO;
import com.siuuuuu.commodeami.actor.query.repository.ActorMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActorServiceImpl implements ActorService {

    private final ActorMapper actorMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public ActorServiceImpl(ActorMapper actorMapper,
                            ModelMapper modelMapper) {
        this.actorMapper = actorMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ActorDTO> getAllActors() {
        List<Actor> actors = actorMapper.selectAllActors();
        List<ActorDTO> actorDTOS =
                actors.stream().map(actor -> modelMapper.map(actor, ActorDTO.class)).collect(Collectors.toList());
        return actorDTOS;
    }

    @Override
    public List<MovieActorDTO> getActorsByMovieId(Long movieId) {
        List<MovieActor> movieActors = actorMapper.selectActorsByMovieId(movieId);
        log.info("배우 쿼리결과 확인: " + movieActors );
        List<MovieActorDTO> movieActorDTOS =
                movieActors.stream().map(movieActor -> modelMapper.map(movieActor, MovieActorDTO.class)).collect(Collectors.toList());
        return movieActorDTOS;
    }
}
