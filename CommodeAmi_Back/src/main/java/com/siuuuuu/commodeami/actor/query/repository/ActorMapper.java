package com.siuuuuu.commodeami.actor.query.repository;

import com.siuuuuu.commodeami.actor.query.aggregate.Actor;
import com.siuuuuu.commodeami.actor.query.aggregate.MovieActor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface ActorMapper {

    List<Actor> selectAllActors();

    List<MovieActor> selectActorsByMovieId(@Param("movieId") Long movieId);
}
