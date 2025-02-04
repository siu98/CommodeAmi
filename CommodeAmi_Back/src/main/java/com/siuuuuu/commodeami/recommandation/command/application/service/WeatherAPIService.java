package com.siuuuuu.commodeami.recommandation.command.application.service;

import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;

import java.util.List;

public interface WeatherAPIService {
//    String getWeatherCondition();
    String getWeatherCondition(double latitude, double longitude);
}
