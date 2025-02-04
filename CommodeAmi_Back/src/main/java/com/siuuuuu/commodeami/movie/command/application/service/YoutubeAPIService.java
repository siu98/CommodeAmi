package com.siuuuuu.commodeami.movie.command.application.service;

import java.util.List;

public interface YoutubeAPIService {

    List<String> fetchYouTubeReviews(String query, int maxResults);
    void saveYouTubeReviews(Long startId, Long endId);
}
