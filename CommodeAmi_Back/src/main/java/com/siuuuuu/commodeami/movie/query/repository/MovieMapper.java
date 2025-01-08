package com.siuuuuu.commodeami.movie.query.repository;

import com.siuuuuu.commodeami.movie.query.aggregate.Movie;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MovieMapper {
    // 영화 전체 조회
    List<Movie> selectAllMovie();
    Movie selectMovieById(Long movieId);
}
