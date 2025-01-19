package com.siuuuuu.commodeami.movie.command.application.service;//package com.siuuuuu_o3o.commodeami.movie.command.application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MovieAPIClient {

    @Value("${tmdb.api.key}")
    private String tmdbApiKey;

    @Value("${tmdb.api.url}")
    private String tmdbApiUrl;

//    @Value("${kobis.api.key}")
//    private String kobisApiKey;
//
//    @Value("${kobis.api.url}")
//    private String kobisApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    @Autowired
    public MovieAPIClient(RestTemplate restTemplate,
                          ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // 인기 영화 가져오기
    public List<Map<String, Object>> fetchPopularMovies() {
        String url = String.format("%s/movie/popular?api_key=%s&language=ko-KR&page=1", tmdbApiUrl, tmdbApiKey);
        Map<String, Object> response = fetchFromApi(url);
        if (response != null) {
            Object results = response.get("results");
            if (results instanceof List) {
                return (List<Map<String, Object>>) results;
            }
        }
        return new ArrayList<>();
    }

    // 영화 상세 정보 가져오기
    public Map<String, Object> fetchMovieDetails(Long movieId) {
        String url = String.format("%s/movie/%d?api_key=%s&language=ko-KR", tmdbApiUrl, movieId, tmdbApiKey);
        return fetchFromApi(url, null);
    }

    // 영화 크레딧 가져오기
    public Map<String, Object> fetchMovieCredits(Long movieId) {
        String url = String.format("%s/movie/%d/credits?api_key=%s", tmdbApiUrl, movieId, tmdbApiKey);
        return fetchFromApi(url, null);
    }

    // 공통 API 호출 메서드
    private Map<String, Object> fetchFromApi(String url, String key) {
        try {
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                Map<String, Object> responseMap = objectMapper.readValue(response, new TypeReference<>() {});
                return key == null ? responseMap : (Map<String, Object>) responseMap.get(key);
            }
        } catch (Exception e) {
            log.error("Error calling API: {}", url, e);
        }
        return null;
    }

    private Map<String, Object> fetchFromApi(String url) {
        try {
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                return objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            }
        } catch (Exception e) {
            log.error("Error calling API: {}", url, e);
        }
        return null;
    }
}
