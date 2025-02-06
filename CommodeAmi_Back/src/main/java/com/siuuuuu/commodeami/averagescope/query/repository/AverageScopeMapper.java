package com.siuuuuu.commodeami.averagescope.query.repository;

import com.siuuuuu.commodeami.averagescope.query.aggregate.AverageScope;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AverageScopeMapper {
    List<AverageScope> selectAllAverageScopes();

    AverageScope selectAverageScopeByMovieId(Long movieId);
}
