package com.siuuuuu.commodeami.movie.command.application.service;

import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;

import java.util.List;

public interface MovieCastService {
    void updateMovieCast(Movie movie, List<?> cast);
}
