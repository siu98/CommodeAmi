package com.siuuuuu.commodeami.movie.command.domain.repository;

import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END FROM Movie m WHERE m.title = :title")
    boolean existsByTitle(@Param("title") String title);
    Optional<Movie> findByMovieId(Long movieId);

    Optional<Movie> findByApiId(Long apiId);

    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) = LOWER(:title)")
    Optional<Movie> findByTitleIgnoreCase(@Param("title") String title);


    List<Movie> findByMovieIdBetween(Long startId, Long endId);
}
