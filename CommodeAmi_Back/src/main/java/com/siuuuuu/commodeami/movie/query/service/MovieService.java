package com.siuuuuu.commodeami.movie.query.service;

import com.siuuuuu.commodeami.movie.query.aggregate.Movie;
import com.siuuuuu.commodeami.movie.query.aggregate.MovieDTO;

import java.util.List;

public interface MovieService {

    MovieDTO findMovieById(Long movieId);

    List<Movie> findAllMovies();
}
