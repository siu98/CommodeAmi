package com.siuuuuu.commodeami.scope.query.repository;

import com.siuuuuu.commodeami.scope.query.aggregate.Scope;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScopeMapper {


    List<Scope> selectAllScopes();

    Scope selectScopesByUserId(@Param("userId") Long userId);

    Scope selectScopesByMovieId(@Param("movieId") Long movieId);

    Scope selectScopesByUserIdAndUserId(@Param("userId") Long userId, @Param("movieId") Long movieId);
}
