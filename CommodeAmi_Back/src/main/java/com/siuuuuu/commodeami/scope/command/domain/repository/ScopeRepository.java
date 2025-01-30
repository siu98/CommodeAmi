package com.siuuuuu.commodeami.scope.command.domain.repository;

import com.siuuuuu.commodeami.scope.command.aggregate.dto.ScopeDTO;
import com.siuuuuu.commodeami.scope.command.aggregate.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
//    Scope findScopeDTOByUserIdAndMovieId(Long userId, Long movieId);
@Query("SELECT r FROM Scope r WHERE r.user.userId = :userId AND r.movie.movieId = :movieId")
    Scope findScopeByUserIdAndMovieId(@Param("userId")Long userId, @Param("movieId") Long movieId);
}
