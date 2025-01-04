package com.siuuuuu.commodeami.movie.command.application.service;

import com.siuuuuu.commodeami.movie.command.aggregate.dto.MovieDetailDTO;
import com.siuuuuu.commodeami.movie.command.aggregate.dto.MovieStillDTO;
import com.siuuuuu.commodeami.movie.command.aggregate.dto.PopularMovieDTO;
import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;

import java.util.List;

public interface APIService {
    List<PopularMovieDTO> fetchPopularMovies();

    MovieDetailDTO fetchMovieDetails(Long movieId);

    void updateMovieCast(Long apiId, Movie movie);

    void updateMovieStills(Long apiId);

    void updateMovieTrailers(Long apiId);
}
