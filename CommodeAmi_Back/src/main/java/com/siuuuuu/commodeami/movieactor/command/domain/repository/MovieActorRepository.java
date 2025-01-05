package com.siuuuuu.commodeami.movieactor.command.domain.repository;


import com.siuuuuu.commodeami.movieactor.command.aggregate.entity.MovieActor;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieActorRepository extends JpaRepository<MovieActor, Long> {

//    Optional<MovieActor> findByMovieIdAndActorId(Long movieId, Long actorId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ma FROM MovieActor ma WHERE ma.movie.movieId = :movieId AND ma.actor.actorId = :actorId")
    Optional<MovieActor> findByMovieIdAndActorId(@Param("movieId") Long movieId, @Param("actorId") Long actorId);

    @Query("SELECT ma FROM MovieActor ma WHERE ma.movie.movieId = :movieId")
    List<MovieActor> findByMovieId(@Param("movieId") Long movieId);
}
