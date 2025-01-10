package com.siuuuuu.commodeami.movie.command.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siuuuuu.commodeami.actor.command.aggregate.dto.ActorDTO;
import com.siuuuuu.commodeami.actor.command.aggregate.entity.Actor;
import com.siuuuuu.commodeami.actor.command.application.service.AppActorService;
import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import com.siuuuuu.commodeami.movieactor.command.aggregate.entity.MovieActor;
import com.siuuuuu.commodeami.movieactor.command.domain.repository.MovieActorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class MovieCastServiceImpl implements MovieCastService {

    private final AppActorService appActorService;
    private final MovieActorRepository movieActorRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public MovieCastServiceImpl(AppActorService appActorService,
                                MovieActorRepository movieActorRepository,
                                ObjectMapper mapper) {
        this.appActorService = appActorService;
        this.movieActorRepository = movieActorRepository;
        this.objectMapper = mapper;
    }

    @Override
    @Transactional
    public void updateMovieCast(Movie movie, List<?> cast) {
        log.info("Updating cast for movie: {}", movie.getTitle());

        for (Object castObj : cast) {
            ActorDTO actorDTO = objectMapper.convertValue(castObj, ActorDTO.class);
            // "Acting" 또는 "Directing" 분야만 처리
            if (!"Acting".equalsIgnoreCase(actorDTO.getKnownForDepartment()) &&
                    !"Directing".equalsIgnoreCase(actorDTO.getKnownForDepartment())) {
                continue;
            }
            // Save or update actor
            Actor actor = appActorService.saveOrUpdateActor(actorDTO);

            // Extract role and casting_order from the cast object
            Map<String, Object> castMap = objectMapper.convertValue(castObj, Map.class);
            String role = castMap.get("character") != null ? castMap.get("character").toString() : null;
            Integer castingOrder = castMap.get("order") != null ? (Integer) castMap.get("order") : null;

            // Check and update movie-actor relationship
            Optional<MovieActor> existingRelationship =
                    movieActorRepository.findByMovieIdAndActorId(movie.getMovieId(), actor.getActorId());

            if (existingRelationship.isEmpty()) {
                MovieActor movieActor = new MovieActor();
                movieActor.setMovie(movie);
                movieActor.setActor(actor);
                movieActor.setRole(role); // 역할 설정
                movieActor.setCastingOrder(castingOrder); // 캐스팅 순서 설정
                movieActorRepository.save(movieActor);
                log.info("Added relationship between movie: {} and actor: {}", movie.getTitle(), actor.getName());
            } else {
                log.info("Relationship already exists for movie: {} and actor: {}", movie.getTitle(), actor.getName());
            }
        }
        log.info("Finished updating cast for movie: {}", movie.getTitle());
    }

}
