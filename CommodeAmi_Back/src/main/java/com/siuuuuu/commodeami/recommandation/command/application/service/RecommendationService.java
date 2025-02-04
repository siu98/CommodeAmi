package com.siuuuuu.commodeami.recommandation.command.application.service;

import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;

import java.util.List;

public interface RecommendationService {
    List<Movie> recommendMovies(double latitude, double longitude);
}
