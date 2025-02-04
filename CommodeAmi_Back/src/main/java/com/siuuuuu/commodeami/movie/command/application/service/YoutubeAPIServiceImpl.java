package com.siuuuuu.commodeami.movie.command.application.service;

import com.siuuuuu.commodeami.movie.command.domain.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class YoutubeAPIServiceImpl implements YoutubeAPIService {

    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;

    @Value("${youtube.api.key")
    private String youtubeApiKey;

    public YoutubeAPIServiceImpl(MovieRepository movieRepository,
                                 RestTemplate restTemplate) {
        this.movieRepository = movieRepository;
        this.restTemplate = restTemplate;
    }



}
